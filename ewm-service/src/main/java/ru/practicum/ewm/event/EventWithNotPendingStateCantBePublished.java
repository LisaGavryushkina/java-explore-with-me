package ru.practicum.ewm.event;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class EventWithNotPendingStateCantBePublished extends ExploreWithMeConflictException {

    public EventWithNotPendingStateCantBePublished(int eventId, State state) {
        super(String.format("Событие [%d] должно быть в состоянии 'PENDING'. Состояние события - '%s'",
                eventId, state));
    }
}
