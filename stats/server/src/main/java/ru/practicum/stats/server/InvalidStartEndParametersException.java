package ru.practicum.stats.server;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InvalidStartEndParametersException extends RuntimeException {
    private final String message;

    public InvalidStartEndParametersException(LocalDateTime start, LocalDateTime end) {
        message = String.format("Дата начала %s позже даты окончания %s", start, end);
    }
}
