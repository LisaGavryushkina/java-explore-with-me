package ru.practicum.ewm.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitForResponseDto;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryNotFoundException;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.pageable.OffsetPageRequest;
import ru.practicum.ewm.request.RequestDto;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.RequestRepository.EventIdWithConfirmedRequests;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserNotFoundException;
import ru.practicum.ewm.user.UserRepository;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventShortedForResponseDto> getInitiatorEvents(int userId, int from, int size) {
        Page<Event> events = eventRepository.findAllByInitiatorId(userId, new OffsetPageRequest(from, size));
        Map<String, Event> urisAndEvents = events.stream()
                .collect(Collectors.toMap(event -> "/events/" + event.getId(), event -> event));
        List<HitForResponseDto> hits = statsClient.getStatistics(LocalDateTime.now().minusDays(10),
                LocalDateTime.now(), new ArrayList<>(urisAndEvents.keySet()), false);
        Map<Integer, Integer> eventIdsAndViews = hits.stream()
                .collect(Collectors.toMap(hit -> urisAndEvents.get(hit.getUri()).getId(), HitForResponseDto::getHits));
        List<EventIdWithConfirmedRequests> confirmedRequests =
                requestRepository.findAllConfirmedByEventIds(new ArrayList<>(eventIdsAndViews.keySet()));
        Map<Integer, Integer> eventIdAndConfirmedRequests = confirmedRequests.stream()
                .collect(Collectors.toMap(EventIdWithConfirmedRequests::getEventId,
                        EventIdWithConfirmedRequests::getConfirmedRequests));
        return eventMapper.toShortedEventDto(events.getContent(), eventIdsAndViews, eventIdAndConfirmedRequests);
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
    public EventForResponseDto getEvent(int userId, int eventId) {
        return null;
    }

    @Override
    public EventForResponseDto updateEventByInitiator(int userId, int eventId, EventForRequestDto eventForRequestDto) {
        return null;
    }

    @Override
    public List<RequestDto> getRequestsForInitiatorEvent(int userId, int eventId) {
        return null;
    }

    @Override
    public UpdatedRequestsStatusDto updateRequestsStatusByInitiator(int userId, int eventId,
                                                                    ToUpdateRequestsStatusDto toUpdateRequestsStatusDto) {
        return null;
    }
}
