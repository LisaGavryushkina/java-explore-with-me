package ru.practicum.ewm.request;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class ParticipantLimitExceededException extends ExploreWithMeConflictException {

    public ParticipantLimitExceededException(int eventId) {
        super(String.format("Превышен лимит запросов на участие в событии [%d]", eventId));
    }
}
