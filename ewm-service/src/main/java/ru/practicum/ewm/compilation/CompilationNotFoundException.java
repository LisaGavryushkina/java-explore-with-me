package ru.practicum.ewm.compilation;

import ru.practicum.ewm.error_handler.ExploreWithMeNotFoundException;

public class CompilationNotFoundException extends ExploreWithMeNotFoundException {

    public CompilationNotFoundException(int id) {
        super(String.format("Подборка [%d] не найдена", id));
    }
}
