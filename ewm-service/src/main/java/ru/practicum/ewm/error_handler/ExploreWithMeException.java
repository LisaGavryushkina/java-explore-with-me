package ru.practicum.ewm.error_handler;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExploreWithMeException extends RuntimeException {

    private final HttpStatus status;
    private final String message;
}
