package ru.practicum.ewm.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitForResponseDto;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryNotFoundException;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;
import ru.practicum.ewm.request.ParticipantLimitExceededException;
import ru.practicum.ewm.request.Request;
import ru.practicum.ewm.request.RequestDto;
import ru.practicum.ewm.request.RequestMapper;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.RequestRepository.EventIdWithConfirmedRequests;
import ru.practicum.ewm.request.Status;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserNotFoundException;
import ru.practicum.ewm.user.UserRepository;

@Service
@RequiredArgsConstructor
@Logged
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventShortedForResponseDto> getInitiatorEvents(int userId, int from, int size) {
        Page<Event> events = eventRepository.findAllByInitiatorId(userId, new OffsetPageRequest(from, size,
                org.springframework.data.domain.Sort.unsorted()));
        Map<String, Map<Integer, Integer>> viewsAndConfirmedRequests =
                getViewsAndConfirmedRequests(events.getContent());
        return eventMapper.toShortedEventDto(events.getContent(), viewsAndConfirmedRequests.get("eventIdsAndViews"),
                viewsAndConfirmedRequests.get("eventIdsAndConfirmedRequests"));
    }

    @Override
    public EventForResponseDto addEvent(int userId, EventForRequestDto eventForRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Category category =
                categoryRepository.findById(eventForRequestDto.getCategory()).orElseThrow(() -> new CategoryNotFoundException(eventForRequestDto.getCategory()));
        Event event = eventRepository.save(eventMapper.toEvent(eventForRequestDto, category, user,
                LocalDateTime.now(), State.WAITING));
        return eventMapper.toEventForResponseDto(event, 0, 0);
    }

    @Override
    public EventForResponseDto getEventByInitiator(int userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        Map<String, Integer> viewsAndConfirmedRequests = getViewsAndConfirmedRequests(eventId);
        return eventMapper.toEventForResponseDto(event,
                viewsAndConfirmedRequests.get("views"), viewsAndConfirmedRequests.get("confirmedRequests"));
    }

    @Override
    public EventForResponseDto updateEventByInitiator(int userId, int eventId, EventForRequestDto eventForRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() == State.PUBLISHED) {
            throw new PublishedEventCantBeUpdatedException(eventId);
        }
        int categoryId = eventForRequestDto.getCategory();
        Category category = null;
        if (categoryId != event.getCategory().getId()) {
            category =
                    categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
        }
        Event toUpdate = eventMapper.updateEventByInitiator(event, eventForRequestDto, category);
        Map<String, Integer> viewsAndConfirmedRequests = getViewsAndConfirmedRequests(eventId);
        return eventMapper.toEventForResponseDto(eventRepository.save(toUpdate),
                viewsAndConfirmedRequests.get("views"), viewsAndConfirmedRequests.get("confirmedRequests"));
    }

    @Override
    public List<RequestDto> getRequestsForInitiatorEvent(int userId, int eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public UpdatedRequestsStatusDto updateRequestsStatusByInitiator(int userId, int eventId,
                                                                    ToUpdateRequestsStatusDto toUpdateRequestsStatusDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventIdWithConfirmedRequests withConfirmedRequests = requestRepository.findAllConfirmedByEventId(eventId);
        int limit = event.getParticipantLimit();
        if (limit != 0 && limit == withConfirmedRequests.getConfirmedRequests()) {
            throw new ParticipantLimitExceededException(eventId);
        }
        List<Request> requests = requestRepository.findAllById(toUpdateRequestsStatusDto.getRequestsIds());
        boolean statusIsPending = requests.stream()
                .allMatch(request -> request.getStatus() == Status.PENDING);
        if (!statusIsPending) {
            throw new NotPendingRequestStatusCantBeUpdated();
        }
        int amountOfConfirmedRequests = withConfirmedRequests.getConfirmedRequests();
        int i = 0;
        List<Request> confirmedRequests = new ArrayList<>();
        while (amountOfConfirmedRequests < limit && i < requests.size()) {
            Request request = requests.get(i);
            request.setStatus(toUpdateRequestsStatusDto.getStatus());
            requestRepository.save(request);
            confirmedRequests.add(request);
            i++;
            amountOfConfirmedRequests++;
        }
        List<Request> pendingRequestsToCancel = new ArrayList<>();
        if (amountOfConfirmedRequests == limit) {
            pendingRequestsToCancel = requestRepository.findAllByEventIdAndStatus(eventId, Status.PENDING);
            if (!pendingRequestsToCancel.isEmpty()) {
                pendingRequestsToCancel.forEach(request -> request.setStatus(Status.CANCELED));
                pendingRequestsToCancel.forEach(requestRepository::save);
            }
        }
        return requestMapper.toUpdatedRequestsStatusDto(confirmedRequests, pendingRequestsToCancel);
    }

    @Override
    public List<EventForResponseDto> getEventsForAdmin(EventFiltersForAdmin eventCriteria, int from, int size) {
        EventForAdminSpecification specification = new EventForAdminSpecification(eventCriteria);
        Page<Event> events = eventRepository.findAll(specification, new OffsetPageRequest(from, size));
        Map<String, Map<Integer, Integer>> viewsAndConfirmedRequests =
                getViewsAndConfirmedRequests(events.getContent());
        return eventMapper.toEventForResponseDto(events.getContent(), viewsAndConfirmedRequests.get("eventIdsAndViews"),
                viewsAndConfirmedRequests.get("eventIdsAndConfirmedRequests"));
    }

    @Override
    public EventForResponseDto updateEventStateByAdmin(int eventId, EventForRequestDto eventForRequestDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        State state;
        switch (eventForRequestDto.getStateAction()) {
            case PUBLISH_EVENT:
                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                    throw new EventDateInLessThanAnHourException(eventId);
                }
                if (event.getState() != State.PENDING) {
                    throw new EventWithNotPendingStateCantBePublished(eventId, event.getState());
                }
                state = State.PUBLISHED;
                break;

            case REJECT_EVENT:
                if (event.getState() == State.PUBLISHED) {
                    throw new PublishedEventCantBeUpdatedException(eventId);
                }
                state = State.CANCELED;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + eventForRequestDto.getStateAction());
        }
        event.setState(state);
        Map<String, Integer> viewsAndConfirmedRequests = getViewsAndConfirmedRequests(eventId);
        return eventMapper.toEventForResponseDto(eventRepository.save(event),
                viewsAndConfirmedRequests.get("views"), viewsAndConfirmedRequests.get("confirmedRequests"));
    }

    @Override
    public List<EventShortedForResponseDto> getEventsForPublic(
            EventFiltersForPublic eventCriteriaForPublic, SortEventsBy sort, int from, int size,
            String uri, String ip) {
        EventForPublicSpecification specification = new EventForPublicSpecification(eventCriteriaForPublic);
        List<EventShortedForResponseDto> responseDtos;
        switch (sort) {
            case EVENT_DATE: {
                Page<Event> events = eventRepository.findAll(specification, new OffsetPageRequest(from, size,
                        Sort.by("event_date")));

                Map<String, Map<Integer, Integer>> viewsAndConfirmedRequests =
                        getViewsAndConfirmedRequests(events.getContent());
                responseDtos = eventMapper.toShortedEventDto(events.getContent(), viewsAndConfirmedRequests.get(
                                "eventIdsAndViews"),
                        viewsAndConfirmedRequests.get("eventIdsAndConfirmedRequests"));
                break;
            }
            case VIEWS: {
                Page<Event> events = eventRepository.findAll(specification, new OffsetPageRequest(from, size));
                Map<Integer, ViewsAndConfirmedRequests> viewsAndConfirmedRequestsByEventIds =
                        getViewsAndConfirmedRequests(events.getContent());
                List<EventShortedForResponseDto> responseDtosByViews =
                        eventMapper.toShortedEventDto(events.getContent(),
                                viewsAndConfirmedRequestsByEventIds.get("eventIdsAndViews"),
                                viewsAndConfirmedRequestsByEventIds.get("eventIdsAndConfirmedRequests"));

                responseDtos = responseDtosByViews.stream()
                        .sorted(Comparator.comparingInt(EventShortedForResponseDto::getViews))
                        .collect(Collectors.toList());
                break;
            }
        }
        statsClient.
    }

    @Override
    public EventForResponseDto getEventForPublic(int id, String uri, String ip) {
        return null;
    }

    private Map<Integer, ViewsAndConfirmedRequests> getViewsAndConfirmedRequests(List<Event> events) {
        Map<String, Event> urisAndEvents = events.stream()
                .collect(Collectors.toMap(event -> "/events/" + event.getId(), event -> event));
        List<HitForResponseDto> hits = statsClient.getStatistics(LocalDateTime.now().minusDays(10),
                LocalDateTime.now(), new ArrayList<>(urisAndEvents.keySet()), false);
        Map<Integer, Integer> eventIdsAndViews = hits.stream()
                .collect(Collectors.toMap(hit -> urisAndEvents.get(hit.getUri()).getId(),
                        HitForResponseDto::getHits));
        List<EventIdWithConfirmedRequests> confirmedRequests =
                requestRepository.findAllConfirmedByEventIds(new ArrayList<>(eventIdsAndViews.keySet()));
        Map<Integer, Integer> eventIdsAndConfirmedRequests = confirmedRequests.stream()
                .collect(Collectors.toMap(EventIdWithConfirmedRequests::getEventId,
                        EventIdWithConfirmedRequests::getConfirmedRequests));
        Map<Integer, ViewsAndConfirmedRequests> viewsAndConfirmedRequestsByEventIds = hits.stream()
                .collect(Collectors.toMap(hit -> urisAndEvents.get(hit.getUri()).getId(), hit ->
                        new ViewsAndConfirmedRequests(hit.getHits(),
                                eventIdsAndConfirmedRequests.getOrDefault(urisAndEvents.get(hit.getUri()).getId(), 0))));

        return viewsAndConfirmedRequestsByEventIds;
    }

    private Map<String, Integer> getViewsAndConfirmedRequests(int eventId) {
        HitForResponseDto hit = statsClient.getStatistics(LocalDateTime.now().minusDays(10),
                LocalDateTime.now(), List.of("/events/" + eventId), false).get(0);
        EventIdWithConfirmedRequests confirmedRequests =
                requestRepository.findAllConfirmedByEventIds(List.of(eventId)).get(0);
        return Map.of("views", hit.getHits(),
                "confirmedRequests", confirmedRequests.getConfirmedRequests());
    }
}
