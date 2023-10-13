package ru.practicum.ewm.error_handler;

public class EwmEntityNotFoundException extends ExploreWithMeNotFoundException {

    public EwmEntityNotFoundException(int id) {
        super(String.format("Сущность [%d] не найдена", id));
    }
}
