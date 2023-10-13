package ru.practicum.ewm.event;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.error_handler.EwmEntityNotFoundException;
import ru.practicum.ewm.event.dto.EventFiltersForAdmin;
import ru.practicum.ewm.event.dto.EventFiltersForPublic;
import ru.practicum.ewm.event.dto.EventForResponseDto;
import ru.practicum.ewm.event.dto.EventShortedForResponseDto;
import ru.practicum.ewm.event.dto.EventToAddDto;
import ru.practicum.ewm.event.dto.StateAction;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.dto.UpdateRequestsStatusDto;
import ru.practicum.ewm.event.dto.UpdatedRequestsStatusDto;
import ru.practicum.ewm.event.exception.EventDateAlreadyPassedException;
import ru.practicum.ewm.event.exception.EventDateInLessThanAnHourException;
import ru.practicum.ewm.event.exception.EventWithNotPendingStateCantBePublished;
import ru.practicum.ewm.event.exception.NotPendingRequestStatusCantBeUpdated;
import ru.practicum.ewm.event.exception.PublishedEventCantBeUpdatedException;
import ru.practicum.ewm.event.specification.EventForAdminSpecification;
import ru.practicum.ewm.event.specification.EventForPublicSpecification;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;
import ru.practicum.ewm.request.Request;
import ru.practicum.ewm.request.RequestDto;
import ru.practicum.ewm.request.RequestMapper;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.Status;
import ru.practicum.ewm.request.exception.ParticipantLimitExceededException;
import ru.practicum.ewm.stats_service.StatsService;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRatingService;
import ru.practicum.ewm.user.UserRepository;

@Service
@RequiredArgsConstructor
@Logged
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final UserRatingService userRatingService;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final CategoryRepository categoryRepository;
    private final StatsService statsService;

    @Override
    public List<EventShortedForResponseDto> getInitiatorEvents(int userId, int from, int size) {
        Page<Event> events = eventRepository.findAllByInitiatorId(userId, new OffsetPageRequest(from, size));
        Map<Integer, Integer> viewsByEventIds = statsService.getViews(events.map(Event::getId).getContent());
        return eventMapper.toShortedEventDtoForInitiator(events.getContent(),
                userRatingService.getLikesAndTotal(userId),
                viewsByEventIds);
    }

    @Override
    @Transactional
    public EventForResponseDto addEvent(int userId, EventToAddDto eventToAddDto) {
        User user = userRepository.findByIdOrThrow(userId);
        Category category = categoryRepository.findByIdOrThrow(eventToAddDto.getCategory());
        Event event = eventRepository.save(eventMapper.toEvent(eventToAddDto, category, user,
                LocalDateTime.now(), State.PENDING));
        return eventMapper.toEventForResponseDto(event, userRatingService.getLikesAndTotal(userId), 0);
    }

    @Override
    public EventForResponseDto getEventByInitiator(int userId, int eventId) {
        userRepository.checkEntityExists(userId);
        Event event = eventRepository.findByIdOrThrow(eventId);
        int views = statsService.getViews(eventId);
        return eventMapper.toEventForResponseDto(event, userRatingService.getLikesAndTotal(userId), views);
    }

    @Override
    @Transactional
    public EventForResponseDto updateEventByInitiator(int userId, int eventId, UpdateEventDto updateEventDto) {
        userRepository.checkEntityExists(userId);
        Event event = eventRepository.findByIdOrThrow(eventId);
        if (event.getState() == State.PUBLISHED) {
            throw new PublishedEventCantBeUpdatedException(eventId);
        }
        State state = event.getState();
        StateAction stateAction = updateEventDto.getStateAction();
        if (stateAction != null) {
            state = determineStateForInitiator(stateAction);
        }
        int newCategoryId = updateEventDto.getCategory();
        Category category = event.getCategory();
        if (newCategoryId != category.getId() && newCategoryId != 0) {
            category = categoryRepository.findByIdOrThrow(newCategoryId);
        }
        Event toUpdate = eventMapper.updateEvent(event, updateEventDto, category, state);
        int views = statsService.getViews(eventId);
        return eventMapper.toEventForResponseDto(eventRepository.save(toUpdate),
                userRatingService.getLikesAndTotal(userId), views);
    }

    @Override
    public List<RequestDto> getRequestsForInitiatorEvent(int userId, int eventId) {
        userRepository.checkEntityExists(userId);
        eventRepository.checkEntityExists(eventId);
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UpdatedRequestsStatusDto updateRequestsStatusByInitiator(int userId, int eventId,
                                                                    UpdateRequestsStatusDto updateRequestsStatusDto) {
        userRepository.checkEntityExists(userId);
        Event event = eventRepository.findByIdOrThrow(eventId);
        int limit = event.getParticipantLimit();
        if (limit != 0 && limit == event.getConfirmedRequests()) {
            throw new ParticipantLimitExceededException(eventId);
        }
        List<Integer> requestsIds = updateRequestsStatusDto.getRequestsIds();
        List<Request> requests;
        if (requestsIds != null && !requestsIds.isEmpty()) {
            requests = requestRepository.findAllById(requestsIds);
        } else {
            requests = requestRepository.findAllByEventId(eventId);
        }
        boolean statusIsPending = requests.stream()
                .allMatch(request -> request.getStatus() == Status.PENDING);
        if (!statusIsPending) {
            throw new NotPendingRequestStatusCantBeUpdated();
        }

        if (updateRequestsStatusDto.getStatus() == Status.REJECTED) {
            return rejectRequests(requests);
        }
        return processRequests(event, requests);
    }

    private UpdatedRequestsStatusDto processRequests(Event event, List<Request> requests) {
        int limit = event.getParticipantLimit();
        List<Request> confirmedRequests;
        List<Request> rejectedRequests = List.of();
        int alreadyConfirmed = event.getConfirmedRequests();
        int eventsToConfirm = Integer.min(limit - alreadyConfirmed, requests.size());
        confirmedRequests = setStatus(requests.subList(0, eventsToConfirm), Status.CONFIRMED);

        if (eventsToConfirm + alreadyConfirmed == limit) {
            rejectedRequests = setStatus(requestRepository.findAllByEventIdAndStatus(event.getId(), Status.PENDING),
                    Status.CANCELED);
        }
        return requestMapper.toUpdatedRequestsStatusDto(confirmedRequests, rejectedRequests);
    }

    private UpdatedRequestsStatusDto rejectRequests(List<Request> requests) {
        requests = setStatus(requests, Status.REJECTED);
        return requestMapper.toUpdatedRequestsStatusDto(List.of(), requests);
    }

    private List<Request> setStatus(List<Request> requests, Status status) {
        requests.forEach(request -> request.setStatus(status));
        return requestRepository.saveAll(requests);
    }

    @Override
    public List<EventForResponseDto> getEventsForAdmin(EventFiltersForAdmin eventFiltersForAdmin, int from, int size) {
        EventForAdminSpecification specification = new EventForAdminSpecification(eventFiltersForAdmin);
        Page<Event> pageEvents = eventRepository.findAll(specification, new OffsetPageRequest(from, size));
        List<Event> events = pageEvents.getContent();
        Map<Integer, Integer> viewsByEventIds = statsService.getViews(events.stream()
                .map(Event::getId)
                .collect(Collectors.toList()));
        return eventMapper.toEventForResponseDto(events, userRatingService.getLikesAndTotal(events), viewsByEventIds);
    }

    @Override
    @Transactional
    public EventForResponseDto updateEventByAdmin(int eventId, UpdateEventDto updateEventDto) {
        Event event = eventRepository.findByIdOrThrow(eventId);
        LocalDateTime newEventDate = updateEventDto.getEventDate();
        if (newEventDate != null) {
            if (newEventDate.isBefore(LocalDateTime.now())) {
                throw new EventDateAlreadyPassedException(eventId, newEventDate);
            }
        }
        StateAction stateAction = updateEventDto.getStateAction();
        State state = event.getState();
        if (stateAction != null) {
            state = determineStateForAdmin(stateAction, event);
        }
        int newCategoryId = updateEventDto.getCategory();
        Category category = event.getCategory();
        if (newCategoryId != category.getId() && newCategoryId != 0) {
            category = categoryRepository.findByIdOrThrow(newCategoryId);
        }
        Event updated = eventMapper.updateEvent(event, updateEventDto, category, state);
        int views = statsService.getViews(eventId);
        return eventMapper.toEventForResponseDto(eventRepository.save(updated),
                userRatingService.getLikesAndTotal(event.getInitiator().getId()), views);
    }

    @Override
    public List<EventShortedForResponseDto> getEventsForPublic(
            EventFiltersForPublic eventFiltersForPublic, SortEventsBy sort, int from, int size,
            String uri, String ip) {

        EventForPublicSpecification specification = new EventForPublicSpecification(eventFiltersForPublic);
        List<Event> events = eventRepository.findAll(specification);
        Map<Integer, Integer> viewsByEventId = statsService.getViews(events.stream()
                .map(Event::getId)
                .collect(Collectors.toList()));
        Comparator<Event> comparator;
        switch (sort) {
            case EVENT_DATE:
                comparator = Comparator.comparing(Event::getEventDate);
                break;
            case VIEWS:
                comparator =
                        Comparator.<Event>comparingInt(event -> viewsByEventId.getOrDefault(event.getId(), 0)).reversed();
                break;
            case RATING:
                comparator = Comparator.<Event>comparingDouble(Event::getRating).reversed();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sort);
        }
        List<Event> eventsToReturn = events.stream()
                .sorted(comparator)
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
        statsService.addHit(uri, ip);
        return eventMapper.toShortedEventDto(eventsToReturn,
                userRatingService.getLikesAndTotal(eventsToReturn),
                viewsByEventId);
    }

    @Override
    public EventForResponseDto getEventForPublic(int eventId, String uri, String ip) {
        Event event = eventRepository.findByIdOrThrow(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new EwmEntityNotFoundException(eventId);
        }
        statsService.addHit(uri, ip);
        int views = statsService.getViews(eventId);
        return eventMapper.toEventForResponseDto(event,
                userRatingService.getLikesAndTotal(event.getInitiator().getId()),
                views);
    }

    private State determineStateForInitiator(StateAction stateAction) {
        switch (stateAction) {
            case REJECT_EVENT:
            case CANCEL_REVIEW:
                return State.CANCELED;
            case SEND_TO_REVIEW:
                return State.PENDING;
            default:
                throw new IllegalStateException("Unexpected value: " + stateAction);
        }
    }

    private State determineStateForAdmin(StateAction stateAction, Event event) {
        switch (stateAction) {
            case PUBLISH_EVENT:
                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                    throw new EventDateInLessThanAnHourException(event.getId());
                }
                if (event.getState() != State.PENDING && event.getState() != State.WAITING) {
                    throw new EventWithNotPendingStateCantBePublished(event.getId(), event.getState());
                }
                return State.PUBLISHED;
            case REJECT_EVENT:
                if (event.getState() == State.PUBLISHED) {
                    throw new PublishedEventCantBeUpdatedException(event.getId());
                }
                return State.CANCELED;
            default:
                throw new IllegalStateException("Unexpected value: " + stateAction);
        }
    }

}
