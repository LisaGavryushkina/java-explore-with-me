package ru.practicum.ewm.compilation.dto;

import java.util.List;

import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortedForResponseDto;

@Data
public class CompilationForResponseDto {

    private final int id;
    private final boolean pinned;
    private final String title;
    private final List<EventShortedForResponseDto> events;
}
