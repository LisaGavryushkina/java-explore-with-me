package ru.practicum;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class HitForRequestDto {
    @NotNull
    private final String app;
    @NotNull
    private final String uri;
    @NotNull
    private final String ip;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

}
