package ru.practicum.ewm.compilation;

import java.util.List;

import ru.practicum.ewm.compilation.dto.CompilationForRequestDto;
import ru.practicum.ewm.compilation.dto.CompilationForResponseDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

public interface CompilationService {

    CompilationForResponseDto addCompilation(CompilationForRequestDto compilationForRequestDto);

    Compilation deleteCompilationById(int compId);

    CompilationForResponseDto updateCompilation(int compId, UpdateCompilationDto compilationForRequestDto);

    List<CompilationForResponseDto> getCompilations(int from, int size, boolean pinned);

    CompilationForResponseDto getCompilationById(int compId);
}
