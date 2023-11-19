package ru.practicum.ewm.like;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class OnlyParticipantsCanRateEventException extends ExploreWithMeConflictException {

    public OnlyParticipantsCanRateEventException() {
        super("Оценивать событие могут только пользователи, принявшие в нем участие");
    }
}
