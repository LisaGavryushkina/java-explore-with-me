package ru.practicum.ewm.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoryDto {
    private final int id;
    @NotBlank
    @Size(max = 50)
    private final String name;
}
