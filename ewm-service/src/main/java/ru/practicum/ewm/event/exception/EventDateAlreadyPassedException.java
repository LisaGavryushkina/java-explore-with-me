package ru.practicum.ewm.event.exception;

import java.time.LocalDateTime;

import ru.practicum.ewm.error_handler.ExploreWithMeNotValidException;

public class EventDateAlreadyPassedException extends ExploreWithMeNotValidException {

    public EventDateAlreadyPassedException(int eventId, LocalDateTime newEventDate) {
        super(String.format("Новая дата %s для изменяемого события [%d] уже наступила", newEventDate, eventId));
    }
}
