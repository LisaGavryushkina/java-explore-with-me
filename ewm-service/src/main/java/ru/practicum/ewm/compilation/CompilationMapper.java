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

import static ru.practicum.ewm.user.UserRepository.LikesAndTotal;

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
                                                                 Map<Integer, LikesAndTotal> likesAndTotalByUserIds,
                                                                 Map<Integer, Integer> viewsByEventIds) {
        List<EventShortedForResponseDto> shorted = eventMapper.toShortedEventDto(events, likesAndTotalByUserIds,
                viewsByEventIds);
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
                                                                       Map<Integer, LikesAndTotal> likesAndTotalByUserIds,
                                                                       Map<Integer, Integer> viewsByEventIds) {
        return compilations.stream()
                .map(compilation -> toCompilationForResponseDto(
                        compilation,
                        eventsByCompilationIds.getOrDefault(compilation.getId(), Collections.emptyList()),
                        likesAndTotalByUserIds,
                        viewsByEventIds))
                .collect(Collectors.toList());
    }
}
