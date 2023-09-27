package ru.practicum.ewm.compilation;

import java.util.List;

import lombok.Data;
import ru.practicum.ewm.event.EventShortedForResponseDto;

@Data
public class CompilationForResponseDto {

    private final int id;
    private final List<EventShortedForResponseDto> events;
    private final boolean pinned;
    private final String title;
}
