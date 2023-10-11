package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.event.validation.HasStartEndRange;
import ru.practicum.ewm.event.validation.StartBeforeEndDateConstraint;

@NoArgsConstructor
@ToString
@Getter
@Setter
@AllArgsConstructor
@StartBeforeEndDateConstraint
public class EventFiltersForPublic implements HasStartEndRange {

    private String text;
    private List<Integer> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
}
