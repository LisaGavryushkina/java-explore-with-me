package ru.practicum.ewm.like;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByParticipantIdAndEventId(int participantId, int eventId);
}
