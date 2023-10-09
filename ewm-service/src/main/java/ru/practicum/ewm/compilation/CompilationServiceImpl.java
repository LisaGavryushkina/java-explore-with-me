package ru.practicum.ewm.compilation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationForRequestDto;
import ru.practicum.ewm.compilation.dto.CompilationForResponseDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;
import ru.practicum.ewm.stats_service.StatsService;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Logged
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationAndEventRepository compilationAndEventRepository;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final StatsService statsService;

    @Override
    public CompilationForResponseDto addCompilation(CompilationForRequestDto compilationForRequestDto) {
        Compilation compilation = compilationRepository.save(compilationMapper.toCompilation(compilationForRequestDto));
        Set<Integer> eventIds = compilationForRequestDto.getEvents();
        if (eventIds != null && !eventIds.isEmpty()) {
            List<Event> events = eventRepository.findAllById(eventIds);
            List<CompilationAndEvent> compilationsAndEvents = compilationMapper.toCompilationAndEvent(compilation,
                    events);
            compilationAndEventRepository.saveAll(compilationsAndEvents);
            return compilationMapper.toCompilationForResponseDto(compilation, events, statsService.getViews(eventIds));
        }
        return compilationMapper.toCompilationForResponseDto(compilation);
    }

    @Override
    public Compilation deleteCompilationById(int compId) {
        Compilation compilation = compilationRepository.findByIdOrThrow(compId);
        compilationAndEventRepository.deleteAllByCompilationId(compId);
        compilationRepository.deleteById(compId);
        return compilation;
    }

    @Override
    public CompilationForResponseDto updateCompilation(int compId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = compilationRepository.findByIdOrThrow(compId);
        Set<Integer> newEventIds = updateCompilationDto.getEvents();
        if (newEventIds != null && !newEventIds.isEmpty()) {
            List<Event> events = eventRepository.findAllById(newEventIds);
            List<CompilationAndEvent> compilationsAndEvents = compilationMapper.toCompilationAndEvent(compilation,
                    events);
            compilationAndEventRepository.saveAll(compilationsAndEvents);
        }
        Compilation updated = compilationMapper.updateCompilation(compilation, updateCompilationDto);
        compilationRepository.save(updated);
        List<Event> events = compilationAndEventRepository.findAllEventByCompilationId(compId);
        Map<Integer, Integer> viewsByEventIds = statsService.getViews(events.stream()
                .map(Event::getId)
                .collect(toList()));
        return compilationMapper.toCompilationForResponseDto(updated, events, viewsByEventIds);
    }

    @Override
    public List<CompilationForResponseDto> getCompilations(int from, int size, boolean pinned) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, new OffsetPageRequest(from,
                size)).getContent();
        List<CompilationAndEvent> compilationAndEvents =
                compilationAndEventRepository.findAllByCompilationIn(compilations);
        List<Integer> eventIds = compilationAndEvents.stream()
                .map(compilationAndEvent -> compilationAndEvent.getEvent().getId())
                .collect(Collectors.toList());
        Map<Integer, List<Event>> eventsByCompilationIds = compilationAndEvents.stream()
                .collect(groupingBy(cae -> cae.getCompilation().getId(),
                        mapping(CompilationAndEvent::getEvent, toList())));
        Map<Integer, Integer> viewsByEventIds = statsService.getViews(eventIds);
        return compilationMapper.toCompilationForResponseDto(compilations, eventsByCompilationIds, viewsByEventIds);
    }

    @Override
    public CompilationForResponseDto getCompilationById(int compId) {
        Compilation compilation = compilationRepository.findByIdOrThrow(compId);
        List<Event> events = compilationAndEventRepository.findAllEventByCompilationId(compId);
        Map<Integer, Integer> viewsByEventIds = statsService.getViews(events.stream()
                .map(Event::getId)
                .collect(toList()));
        return compilationMapper.toCompilationForResponseDto(compilation, events, viewsByEventIds);
    }
}
