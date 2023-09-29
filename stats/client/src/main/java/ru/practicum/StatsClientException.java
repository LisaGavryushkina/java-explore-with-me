package ru.practicum;

import lombok.Data;

@Data
public class StatsClientException extends RuntimeException {
    private final String message;
}
