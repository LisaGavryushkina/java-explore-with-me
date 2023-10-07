package ru.practicum.ewm.event.exception;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;
import ru.practicum.ewm.event.State;

public class EventWithNotPendingStateCantBePublished extends ExploreWithMeConflictException {

    public EventWithNotPendingStateCantBePublished(int eventId, State state) {
        super(String.format("Событие [%d] должно быть в состоянии 'PENDING'. Состояние события - '%s'",
                eventId, state));
    }
}
