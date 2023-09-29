package ru.practicum.ewm.event;

import java.time.LocalDateTime;

import lombok.Data;
import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.user.UserDto;

@Data
public class EventForResponseDto {

    private final int id;
    private final String annotation;
    private final CategoryDto category;
    private final int confirmedRequests;
    private final LocalDateTime createdOn;
    private final String description;
    private final LocalDateTime eventDate;
    private final UserDto initiator;
    private final Location location;
    private final boolean paid;
    private final int participantLimit;
    private final LocalDateTime publishedOn;
    private final boolean requestModeration;
    private final State state;
    private final String title;
    private final int views;

}
