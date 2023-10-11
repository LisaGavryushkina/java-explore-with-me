package ru.practicum.ewm.error_handler;

import org.springframework.http.HttpStatus;

public class ExploreWithMeNotValidException extends ExploreWithMeException {

    public ExploreWithMeNotValidException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
