package ru.practicum.ewm.event;

import ru.practicum.ewm.error_handler.ExploreWithMeNotFoundException;

public class EventNotFoundException extends ExploreWithMeNotFoundException {

    public EventNotFoundException(int id) {
        super(String.format("Событие [%d] не найдено", id));
    }
}
