package ru.practicum.ewm.category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(),
                category.getName());
    }

    public Category toCategory(CategoryDto categoryDto) {
        return new Category(0,
                categoryDto.getName());
    }
}
