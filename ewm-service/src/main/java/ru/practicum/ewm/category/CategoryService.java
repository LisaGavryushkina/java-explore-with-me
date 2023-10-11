package ru.practicum.ewm.category;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto deleteCategory(int catId);

    CategoryDto updateCategory(int catId, CategoryDto categoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(int catId);
}
