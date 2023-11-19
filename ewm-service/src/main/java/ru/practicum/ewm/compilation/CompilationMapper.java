package ru.practicum.ewm.compilation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilation.dto.CompilationForRequestDto;
import ru.practicum.ewm.compilation.dto.CompilationForResponseDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.dto.EventShortedForResponseDto;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation toCompilation(CompilationForRequestDto compilationForRequestDto) {
        return new Compilation(0,
                compilationForRequestDto.isPinned(),
                compilationForRequestDto.getTitle());
    }

    public List<CompilationAndEvent> toCompilationAndEvent(Compilation compilation, List<Event> events) {
        return events.stream()
                .map(event -> new CompilationAndEvent(0, compilation, event))
                .collect(Collectors.toList());
    }

    public CompilationForResponseDto toCompilationForResponseDto(Compilation compilation, List<Event> events,
                                                                 Map<Integer, Float> ratingsByUserIds,
                                                                 Map<Integer, Integer> viewsByEventIds,
                                                                 Map<Integer, Float> ratingsByEventIds) {
        List<EventShortedForResponseDto> shorted = eventMapper.toShortedEventDto(events, ratingsByUserIds,
                viewsByEventIds, ratingsByEventIds);
        return new CompilationForResponseDto(compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle(),
                shorted);
    }

    public CompilationForResponseDto toCompilationForResponseDto(Compilation compilation) {
        return new CompilationForResponseDto(compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle(),
                Collections.emptyList());
    }

    public Compilation updateCompilation(Compilation compilation, UpdateCompilationDto updateCompilationDto) {
        compilation.setPinned(updateCompilationDto.isPinned());
        String title = updateCompilationDto.getTitle();
        if (title != null) {
            compilation.setTitle(title);
        }
        return compilation;
    }

    public List<CompilationForResponseDto> toCompilationForResponseDto(List<Compilation> compilations,
                                                                       Map<Integer, List<Event>> eventsByCompilationIds,
                                                                       Map<Integer, Float> ratingsByUserIds,
                                                                       Map<Integer, Integer> viewsByEventIds,
                                                                       Map<Integer, Float> ratingsByEventIds) {
        return compilations.stream()
                .map(compilation -> toCompilationForResponseDto(
                        compilation,
                        eventsByCompilationIds.getOrDefault(compilation.getId(), Collections.emptyList()),
                        ratingsByUserIds,
                        viewsByEventIds,
                        ratingsByEventIds))
                .collect(Collectors.toList());
    }
}
