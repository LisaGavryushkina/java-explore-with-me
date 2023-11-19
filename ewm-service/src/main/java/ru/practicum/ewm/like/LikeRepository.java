package ru.practicum.ewm.like;

import java.util.Optional;

import ru.practicum.ewm.repository.EwmEntityRepository;

public interface LikeRepository extends EwmEntityRepository<Like> {

    Optional<Like> findByParticipantIdAndEventId(int participantId, int eventId);
}
