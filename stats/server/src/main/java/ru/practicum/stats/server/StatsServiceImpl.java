package ru.practicum.stats.server;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.HitForRequestDto;
import ru.practicum.HitForResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @Override
    public void addHit(HitForRequestDto hitForRequestDto) {
        Hit hit = hitMapper.toHit(hitForRequestDto);
        log.info("Добавлен новый просмотр: {}", statsRepository.save(hit));
    }

    @Override
    public List<HitForResponseDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                 boolean unique) {
        List<HitForResponseDto> hitForResponseDtos;
        if (uris != null) {
            if (unique) {
                hitForResponseDtos = statsRepository.findAllUniqueHitsByDateAndUris(start, end, uris);
            } else {
                hitForResponseDtos = statsRepository.findAllHitsByDateAndUris(start, end, uris);
            }
        } else {
            if (unique) {
                hitForResponseDtos = statsRepository.findAllUniqueHitsByDate(start, end);
            } else {
                hitForResponseDtos = statsRepository.findAllHitsByDate(start, end);
            }
        }
        log.info("Вернули статистику: {}", hitForResponseDtos);
        return hitForResponseDtos;
    }
}
