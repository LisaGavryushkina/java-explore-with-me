package ru.practicum.ewm.event.validation;

import java.time.LocalDateTime;

public interface HasStartEndRange {
    LocalDateTime getRangeStart();

    LocalDateTime getRangeEnd();
}