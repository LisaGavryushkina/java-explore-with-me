package ru.practicum.ewm.like;

import lombok.Data;

@Data
public class LikeDto {

    private final int id;
    private final int participantId;
    private final int eventId;
    private final boolean isLike;
}
