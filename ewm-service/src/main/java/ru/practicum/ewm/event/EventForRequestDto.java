package ru.practicum.ewm.event;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Getter
@AllArgsConstructor
public class EventForRequestDto {

    @NotNull
    private String annotation;
    private int category;
    @NotNull
    private String description;
    @EventDateConstraint
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private StateAction stateAction;
    @NotNull
    private String title;
}
