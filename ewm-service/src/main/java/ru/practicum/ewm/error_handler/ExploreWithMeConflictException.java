package ru.practicum.ewm.error_handler;

import org.springframework.http.HttpStatus;

public class ExploreWithMeConflictException extends ExploreWithMeException {

    public ExploreWithMeConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
