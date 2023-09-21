package ru.practicum.stats.server;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.HitForRequestDto;
import ru.practicum.HitForResponseDto;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public void addHit(@Valid @RequestBody HitForRequestDto hitForRequestDto) {
        statsService.addHit(hitForRequestDto);
    }

    @GetMapping("/stats")
    public List<HitForResponseDto> getStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        return statsService.getStatistics(start, end, uris, unique);
    }
}
