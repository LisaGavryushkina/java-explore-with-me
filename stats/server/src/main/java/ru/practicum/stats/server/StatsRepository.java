package ru.practicum.stats.server;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.HitForResponseDto;

public interface StatsRepository extends JpaRepository<Hit, Integer> {

    @Query(nativeQuery = true, value = "" +
            " select h.app as app, " +
            " h.uri as uri, " +
            " count(h.ip) as hits" +
            " from hit as h " +
            " where uri in :uris " +
            " and h.visited between :start and :end " +
            " group by uri ")
    List<HitForResponseDto> findAllHitsByDateAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, value = "" +
            " select h.app as app, " +
            " h.uri as uri, " +
            " count (distinct h.ip) as hits" +
            " from hit as h " +
            " where uri in :uris " +
            " and h.visited between :start and :end " +
            " group by uri ")
    List<HitForResponseDto> findAllUniqueHitsByDateAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, value = "" +
            " select h.app as app, " +
            " h.uri as uri, " +
            " count (distinct h.ip) as hits" +
            " from hit as h " +
            " where h.visited between :start and :end " +
            " group by uri ")
    List<HitForResponseDto> findAllUniqueHitsByDate(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, value = "" +
            " select h.app as app, " +
            " h.uri as uri, " +
            " count(h.ip) as hits" +
            " from hit as h " +
            " where h.visited between :start and :end " +
            " group by uri ")
    List<HitForResponseDto> findAllHitsByDate(LocalDateTime start, LocalDateTime end);
}
