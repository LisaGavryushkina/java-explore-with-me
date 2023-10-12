package ru.practicum.ewm.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.event.dto.EventForResponseDto;
import ru.practicum.ewm.event.dto.EventShortedForResponseDto;
import ru.practicum.ewm.event.dto.EventToAddDto;
import ru.practicum.ewm.event.dto.Location;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public List<EventShortedForResponseDto> toShortedEventDto(List<Event> events,
                                                              Map<Integer, Integer> eventIdsAndViews) {
        return events.stream()
                .map(event -> new EventShortedForResponseDto(event.getId(),
                        event.getAnnotation(),
                        categoryMapper.toCategoryDto(event.getCategory()),
                        event.getConfirmedRequests(),
                        event.getEventDate(),
                        userMapper.toUserDto(event.getInitiator()),
                        event.isPaid(),
                        event.getTitle(),
                        eventIdsAndViews.getOrDefault(event.getId(), 0),
                        event.getRating()))
                .collect(Collectors.toList());
    }

    public EventShortedForResponseDto toShortedEventDto(Event event, int views) {
        return new EventShortedForResponseDto(event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                userMapper.toUserDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                views,
                event.getRating());
    }

    public Event toEvent(EventToAddDto eventForRequestDto, Category category, User user, LocalDateTime now,
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
                0,
                null,
                eventForRequestDto.isRequestModeration(),
                state,
                eventForRequestDto.getTitle(),
                0,
                0);
    }

    public EventForResponseDto toEventForResponseDto(Event event, int views) {
        return new EventForResponseDto(event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
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
                views,
                event.getRating());
    }

    public List<EventForResponseDto> toEventForResponseDto(List<Event> events, Map<Integer, Integer> eventIdsAndViews) {
        return events.stream()
                .map(event -> toEventForResponseDto(event, eventIdsAndViews.getOrDefault(event.getId(), 0)))
                .collect(Collectors.toList());
    }

    public Event updateEvent(Event event, UpdateEventDto updateEventDto,
                             Category category, State state) {
        String annotation = updateEventDto.getAnnotation();
        String description = updateEventDto.getDescription();
        LocalDateTime eventDate = updateEventDto.getEventDate();
        Location location = updateEventDto.getLocation();
        int participantLimit = updateEventDto.getParticipantLimit();
        String title = updateEventDto.getTitle();
        Boolean paid = updateEventDto.getPaid();

        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        event.setCategory(category);
        if (description != null) {
            event.setDescription(description);
        }
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }
        if (location != null) {
            event.setLat(location.getLat());
            event.setLon(location.getLon());
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != 0) {
            event.setParticipantLimit(participantLimit);
        }
        if (title != null) {
            event.setTitle(title);
        }
        event.setState(state);
        return event;
    }
}
