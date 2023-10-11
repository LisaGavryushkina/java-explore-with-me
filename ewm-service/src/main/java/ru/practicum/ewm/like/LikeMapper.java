package ru.practicum.ewm.like;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.User;

@Component
public class LikeMapper {
    public Like toLike(User participant, Event event, boolean isLike) {
        return new Like(0,
                participant,
                event,
                isLike);
    }

    public LikeDto toLikeDto(Like like) {
        return new LikeDto(like.getId(),
                like.getParticipant().getId(),
                like.getEvent().getId(),
                like.isLike());
    }
}
