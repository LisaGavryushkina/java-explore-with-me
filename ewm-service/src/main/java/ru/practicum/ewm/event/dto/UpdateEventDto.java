package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.event.validation.AnnotationConstraint;
import ru.practicum.ewm.event.validation.DescriptionConstraint;
import ru.practicum.ewm.event.validation.TitleConstraint;
import ru.practicum.ewm.event.validation.UpdateEventDateConstraint;

@NoArgsConstructor
@ToString
@Getter
@AllArgsConstructor
public class UpdateEventDto {
    @AnnotationConstraint
    private String annotation;
    private int category;
    @DescriptionConstraint
    private String description;
    @UpdateEventDateConstraint
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private StateAction stateAction;
    @TitleConstraint
    private String title;
}
