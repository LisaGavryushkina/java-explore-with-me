package ru.practicum.ewm.category;

import ru.practicum.ewm.error_handler.ExploreWithMeConflictException;

public class CategoryIsNotEmptyException extends ExploreWithMeConflictException {

    public CategoryIsNotEmptyException(int id) {
        super(String.format("Категория [%d] связана с событиями", id));
    }

}
