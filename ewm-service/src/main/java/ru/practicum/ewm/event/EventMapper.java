package ru.practicum.ewm.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public EventShortedForResponseDto toShortedEventDto(Event event, int confirmedRequests, int views) {
        return new EventShortedForResponseDto(event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getEventDate(),
                userMapper.toUserDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                views);
    }

    public List<EventShortedForResponseDto> toShortedEventDto(List<Event> events,
                                                              Map<Integer, Integer> eventIdsAndViews,
                                                              Map<Integer, Integer> eventIdAndConfirmedRequests) {
        return events.stream()
                .map(event -> new EventShortedForResponseDto(event.getId(),
                        event.getAnnotation(),
                        categoryMapper.toCategoryDto(event.getCategory()),
                        eventIdAndConfirmedRequests.get(event.getId()),
                        event.getEventDate(),
                        userMapper.toUserDto(event.getInitiator()),
                        event.isPaid(),
                        event.getTitle(),
                        eventIdsAndViews.get(event.getId())))
                .collect(Collectors.toList());
    }

    public Event toEvent(EventForRequestDto eventForRequestDto, Category category, User user, LocalDateTime now,
                         State state) {
        return new Event(0,
                eventForRequestDto.getAnnotation(),
                category,
                now,
                eventForRequestDto.getDescription(),
                eventForRequestDto.getEventDate(),
                user,
                eventForRequestDto.getLocation().getLat(),
                eventForRequestDto.getLocation().getLon(),
                eventForRequestDto.isPaid(),
                eventForRequestDto.getParticipantLimit(),
                null,
                eventForRequestDto.isRequestModeration(),
                state,
                eventForRequestDto.getTitle());
    }

    public EventForResponseDto toEventForResponseDto(Event event, int confirmedRequests, int views) {
        return new EventForResponseDto(event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                userMapper.toUserDto(event.getInitiator()),
                new Location(event.getLat(), event.getLon()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                views);
    }
}
