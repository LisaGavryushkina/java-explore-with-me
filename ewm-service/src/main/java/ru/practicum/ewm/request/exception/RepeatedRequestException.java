package ru.practicum.ewm.request.exception;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class RepeatedRequestException extends ExploreWithMeConflictException {

    public RepeatedRequestException(int userId, int eventId) {
        super(String.format("Запрос от пользователя [%d] на участие в событии [%d] уже существует", userId, eventId));
    }

}
