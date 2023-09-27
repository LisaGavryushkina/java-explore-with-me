package ru.practicum.ewm.compilation;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CompilationForRequestDto {
    @NotNull
    private final Set<Integer> events;
    @NotNull
    private final boolean pinned;
    @NotNull
    private final String title;
}
