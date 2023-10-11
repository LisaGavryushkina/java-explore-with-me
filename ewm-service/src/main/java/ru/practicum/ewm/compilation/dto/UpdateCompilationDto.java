package ru.practicum.ewm.compilation.dto;

import java.util.Set;

import lombok.Data;
import ru.practicum.ewm.compilation.validation.CompilationTitleConstraint;

@Data
public class UpdateCompilationDto {
    private final Set<Integer> events;
    private final boolean pinned;
    @CompilationTitleConstraint
    private final String title;

}
