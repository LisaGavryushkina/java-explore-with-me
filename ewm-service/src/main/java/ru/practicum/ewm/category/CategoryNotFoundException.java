package ru.practicum.ewm.category;

import ru.practicum.ewm.error_handler.ExploreWithMeNotFoundException;

public class CategoryNotFoundException extends ExploreWithMeNotFoundException {

    public CategoryNotFoundException(int id) {
        super(String.format("Категория [%d] не найдена", id));
    }
}
