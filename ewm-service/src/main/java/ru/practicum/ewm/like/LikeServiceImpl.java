package ru.practicum.ewm.like;

import java.util.Optional;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.request.Request;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.Status;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

@Service
@RequiredArgsConstructor
@Logged
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;


    @Override
    @Transactional
    public LikeDto addLike(int participantId, int eventId, boolean isLike) {
        User participant = userRepository.findByIdOrThrow(participantId);
        Event event = eventRepository.findByIdOrThrow(eventId);
        Optional<Request> optionalRequest = requestRepository.findByRequesterIdAndEventId(participantId, eventId);
        if (optionalRequest.isEmpty() || optionalRequest.get().getStatus() != Status.CONFIRMED) {
            throw new OnlyParticipantsCanRateEventException();
        }
        Optional<Like> optionalLike = likeRepository.findByParticipantIdAndEventId(participantId, eventId);
        if (optionalLike.isPresent()) {
            throw new DuplicateRatingException();
        }
        Like like = likeRepository.save(likeMapper.toLike(participant, event, isLike));
        return likeMapper.toLikeDto(like);
    }

    @Override
    @Transactional
    public Like deleteLike(int likeId) {
        Like like = likeRepository.findByIdOrThrow(likeId);
        likeRepository.deleteById(likeId);
        return like;
    }

    @Override
    public LikeDto getLikeById(int likeId) {
        Like like = likeRepository.findByIdOrThrow(likeId);
        return likeMapper.toLikeDto(like);
    }
}
