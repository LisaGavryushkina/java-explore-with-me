package ru.practicum.ewm.user;

import ru.practicum.ewm.error_handler.ExploreWithMeNotFoundException;

public class UserNotFoundException extends ExploreWithMeNotFoundException {

    public UserNotFoundException(int id) {
        super(String.format("Пользователь [%d] не найден", id));
    }

}

