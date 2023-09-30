package ru.practicum.ewm.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query(nativeQuery = true, value = "" +
            " select r.event_id as event_id," +
            " count(r.id) as confirmed_requests" +
            " where event_id in :eventIds " +
            " and r.status = 'CONFIRMED' ")
    List<EventIdWithConfirmedRequests> findAllConfirmedByEventIds(List<Integer> eventIds);

    @Query(nativeQuery = true, value = "" +
            " select r.event_id as event_id," +
            " count(r.id) as confirmed_requests" +
            " where event_id = :eventId " +
            " and r.status = 'CONFIRMED' ")
    EventIdWithConfirmedRequests findAllConfirmedByEventId(int eventId);

    List<Request> findAllByRequesterId(int userId);

    boolean existsByRequesterIdAndEventId(int userId, int eventId);

    interface EventIdWithConfirmedRequests {

        int getEventId();

        int getConfirmedRequests();

    }
}
