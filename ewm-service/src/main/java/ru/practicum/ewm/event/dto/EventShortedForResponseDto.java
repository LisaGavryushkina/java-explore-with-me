package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;

import lombok.Data;
import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.user.UserDto;

@Data
public class EventShortedForResponseDto {

    private final int id;
    private final String annotation;
    private final CategoryDto category;
    private final int confirmedRequests;
    private final LocalDateTime eventDate;
    private final UserDto initiator;
    private final boolean paid;
    private final String title;
    private final int views;
    private final float rating;

}
