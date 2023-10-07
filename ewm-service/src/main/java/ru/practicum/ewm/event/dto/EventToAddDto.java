package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.event.Location;
import ru.practicum.ewm.event.StateAction;
import ru.practicum.ewm.event.validation.AddEventDateConstraint;

@NoArgsConstructor
@ToString
@Getter
@AllArgsConstructor
public class EventToAddDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    private int category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @AddEventDateConstraint
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration = true;
    private StateAction stateAction;
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
