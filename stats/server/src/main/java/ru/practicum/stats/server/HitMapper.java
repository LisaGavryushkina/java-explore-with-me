package ru.practicum.stats.server;

import org.springframework.stereotype.Component;
import ru.practicum.HitForRequestDto;
@Component
public class HitMapper {

    public Hit toHit(HitForRequestDto hitForRequestDto) {
        return new Hit(0,
                hitForRequestDto.getApp(),
                hitForRequestDto.getUri(),
                hitForRequestDto.getIp(),
                hitForRequestDto.getTimestamp());
    }
}
