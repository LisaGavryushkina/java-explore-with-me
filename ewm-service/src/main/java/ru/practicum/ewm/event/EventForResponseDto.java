package ru.practicum.ewm.event;

import java.time.LocalDateTime;

import lombok.Data;
import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.user.UserDto;

@Data
public class EventForResponseDto {

    private int id;
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private State state;
    private String title;
    private int views;

}
