package ru.practicum.ewm.event.exception;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class EventDateInLessThanAnHourException extends ExploreWithMeConflictException {

    public EventDateInLessThanAnHourException(int eventId) {
        super(String.format("Время начала события [%d] менее, чем через 1 час", eventId));
    }
}
