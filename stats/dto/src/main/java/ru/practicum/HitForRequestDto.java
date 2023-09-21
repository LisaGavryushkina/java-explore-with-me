package ru.practicum;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class HitForRequestDto {
    @NotNull
    private final String app;
    @NotNull
    private final String uri;
    @NotNull
    private final String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime timestamp;

}
