package ru.practicum.ewm.like;

import ru.practicum.ewm.error_handler.ExploreWithMeNotFoundException;

public class LikeNotFoundException extends ExploreWithMeNotFoundException {

    public LikeNotFoundException(int id) {
        super(String.format("Лайк [%d] не найден", id));
    }
}
