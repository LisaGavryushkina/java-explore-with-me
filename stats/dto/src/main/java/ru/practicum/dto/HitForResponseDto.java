package ru.practicum.dto;

import lombok.Data;

@Data
public class HitForResponseDto {
    private final String app;
    private final String uri;
    private final int hits;
}
