package ru.practicum.ewm.request;

import ru.practicum.ewm.error_handler.ExploreWithMeNotFoundException;

public class RequestNotFoundException extends ExploreWithMeNotFoundException {

    public RequestNotFoundException(int id) {
        super(String.format("Запрос на участие [%d] не найден", id));
    }
}