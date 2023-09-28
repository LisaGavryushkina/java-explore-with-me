package ru.practicum.ewm.error_handler;

import org.springframework.http.HttpStatus;

public class ExploreWithMeNotFoundException extends ExploreWithMeException {

    public ExploreWithMeNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
