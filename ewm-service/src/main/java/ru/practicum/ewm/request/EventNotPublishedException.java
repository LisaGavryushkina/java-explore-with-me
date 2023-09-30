package ru.practicum.ewm.request;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class EventNotPublishedException extends ExploreWithMeConflictException {

    public EventNotPublishedException(int eventId) {
        super(String.format("Событие [%d] не опубликовано", eventId));
    }
}
