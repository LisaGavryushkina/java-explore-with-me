package ru.practicum.ewm.like;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class DuplicateRatingException extends ExploreWithMeConflictException {

    public DuplicateRatingException() {
        super("Оценка от текущего пользователя уже существует");
    }
}
