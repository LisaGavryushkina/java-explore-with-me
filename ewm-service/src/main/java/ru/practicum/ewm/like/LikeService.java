package ru.practicum.ewm.like;

public interface LikeService {

    LikeDto addLike(int participantId, int eventId, boolean isLike);

    Like deleteLike(int likeId);

    LikeDto getLikeById(int likeId);
}
