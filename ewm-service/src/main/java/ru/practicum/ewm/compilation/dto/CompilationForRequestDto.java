package ru.practicum.ewm.compilation.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CompilationForRequestDto {
    private final Set<Integer> events;
    private final boolean pinned;
    @NotBlank
    @Size(max = 50)
    private final String title;
}
