package ru.practicum.ewm.event.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.validation.HasStartEndRange;
import ru.practicum.ewm.event.validation.StartBeforeEndDateConstraint;

@NoArgsConstructor
@ToString
@Getter
@Setter
@AllArgsConstructor
@StartBeforeEndDateConstraint
public class EventFiltersForAdmin implements HasStartEndRange {

    private List<State> states;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private List<Integer> users;
    private List<Integer> categories;
}
