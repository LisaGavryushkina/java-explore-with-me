package ru.practicum.ewm.request;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequesterId(int userId);

    boolean existsByRequesterIdAndEventId(int userId, int eventId);

    List<Request> findAllByEventId(int eventId);

    List<Request> findAllByEventIdAndStatus(int eventId, Status status);

    Optional<Request> findByRequesterIdAndEventId(int requesterId, int eventId);
}
