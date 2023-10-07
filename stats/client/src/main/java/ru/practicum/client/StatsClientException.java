package ru.practicum.client;

import lombok.Data;

@Data
public class StatsClientException extends RuntimeException {
    private final String message;
    private final Throwable cause;
}
