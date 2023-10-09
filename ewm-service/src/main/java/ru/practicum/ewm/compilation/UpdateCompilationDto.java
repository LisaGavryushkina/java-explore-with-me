package ru.practicum.ewm.compilation;

import java.util.Set;

import lombok.Data;

@Data
public class UpdateCompilationDto {
    private final Set<Integer> events;
    private final boolean pinned;
    @CompilationTitleConstraint
    private final String title;

}
