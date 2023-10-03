package ru.practicum.ewm.event;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class PublishedEventCantBeUpdatedException extends ExploreWithMeConflictException {

    public PublishedEventCantBeUpdatedException(int eventId) {
        super(String.format("Событие [%d] уже опубликовано и не можеть быть изменено", eventId));
    }
}
