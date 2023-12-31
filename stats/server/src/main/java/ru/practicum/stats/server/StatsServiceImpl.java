package ru.practicum.stats.server;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitForRequestDto;
import ru.practicum.dto.HitForResponseDto;

import static ru.practicum.stats.server.StatsRepository.HitsByUri;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @Transactional
    @Override
    public void addHit(HitForRequestDto hitForRequestDto) {
        Hit saved = statsRepository.save(hitMapper.toHit(hitForRequestDto));
        log.info("Добавлен новый просмотр: {}", saved);
    }

    @Override
    public List<HitForResponseDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                 boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidStartEndParametersException(start, end);
        }
        List<HitsByUri> hitsByUris;
        if (uris != null && !uris.isEmpty()) {
            if (unique) {
                hitsByUris = statsRepository.findAllUniqueHitsByDateAndUris(start, end, uris);
            } else {
                hitsByUris = statsRepository.findAllHitsByDateAndUris(start, end, uris);
            }
        } else {
            if (unique) {
                hitsByUris = statsRepository.findAllUniqueHitsByDate(start, end);
            } else {
                hitsByUris = statsRepository.findAllHitsByDate(start, end);
            }
        }
        List<HitForResponseDto> hitForResponseDtos =
                hitsByUris.stream().map(hitMapper::toHitResponseDto).collect(Collectors.toList());
        log.info("Вернули статистику: {}", hitForResponseDtos);
        return hitForResponseDtos;
    }
}
