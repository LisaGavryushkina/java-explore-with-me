package ru.practicum.ewm.event.exception;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class NotPendingRequestStatusCantBeUpdated extends ExploreWithMeConflictException {

    public NotPendingRequestStatusCantBeUpdated() {
        super("Запрос должен быть в статусе 'PENDING'");
    }
}
