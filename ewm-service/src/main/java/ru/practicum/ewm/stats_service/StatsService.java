package ru.practicum.ewm.stats_service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitForRequestDto;
import ru.practicum.dto.HitForResponseDto;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsClient statsClient;
    private static final LocalDateTime START = LocalDateTime.now().minusYears(1);
    private static final boolean UNIQUE = true;
    public static final String APP = "ewm-main-service";

    public Map<Integer, Integer> getViews(Collection<Integer> eventIds) {
        Set<Integer> eventIdsSet = new HashSet<>(eventIds);
        Map<String, Integer> eventIdsByUris = eventIdsSet.stream()
                .collect(Collectors.toMap(id -> "/events/" + id, id -> id));
        List<HitForResponseDto> hits = statsClient.getStatistics(START,
                LocalDateTime.now(), eventIdsByUris.keySet(), UNIQUE);

        return hits.stream()
                .filter(hit -> eventIdsByUris.get(hit.getUri()) != null)
                .collect(Collectors.toMap(hit -> eventIdsByUris.get(hit.getUri()), HitForResponseDto::getHits));
    }

    public int getViews(int eventId) {
        List<HitForResponseDto> hits = statsClient.getStatistics(START,
                LocalDateTime.now(), List.of("/events/" + eventId), UNIQUE);
        if (hits.isEmpty()) {
            return 0;
        }
        return hits.get(0).getHits();
    }

    public HitForResponseDto addHit(String uri, String ip) {
        HitForRequestDto hit = new HitForRequestDto(APP, uri, ip, LocalDateTime.now());
        return statsClient.addHit(hit);
    }
}
