package ru.practicum.ewm.request.exception;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class RequestFromInitiatorException extends ExploreWithMeConflictException {

    public RequestFromInitiatorException(int userId, int eventId) {
        super(String.format("Пользователь [%d] является инициатором события [%d] и не может добавить запрос на " +
                "участие в своем событии", userId, eventId));
    }
}
