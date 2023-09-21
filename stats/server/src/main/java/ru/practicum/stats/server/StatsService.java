package ru.practicum.stats.server;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.HitForRequestDto;
import ru.practicum.HitForResponseDto;

public interface StatsService {

    void addHit(HitForRequestDto hitForRequestDto);

    List<HitForResponseDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
