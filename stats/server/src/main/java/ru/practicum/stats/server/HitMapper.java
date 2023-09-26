package ru.practicum.stats.server;

import org.springframework.stereotype.Component;
import ru.practicum.HitForRequestDto;
import ru.practicum.HitForResponseDto;

import static ru.practicum.stats.server.StatsRepository.HitsByUri;

@Component
public class HitMapper {

    public Hit toHit(HitForRequestDto hitForRequestDto) {
        return new Hit(0,
                hitForRequestDto.getApp(),
                hitForRequestDto.getUri(),
                hitForRequestDto.getIp(),
                hitForRequestDto.getTimestamp());
    }

    public HitForResponseDto toHitResponseDto(HitsByUri hitsByUri) {
        return new HitForResponseDto(hitsByUri.getApp(),
                hitsByUri.getUri(),
                hitsByUri.getHits());
    }
}
