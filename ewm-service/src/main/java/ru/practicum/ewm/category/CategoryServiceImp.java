package ru.practicum.ewm.category;

import java.util.List;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;

@Service
@RequiredArgsConstructor
@Logged
@Transactional
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.toCategory(categoryDto));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto deleteCategory(int catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId));
        List<Event> categoryEvents = eventRepository.findAllByCategoryId(catId);
        if (!categoryEvents.isEmpty()) {
            throw new CategoryIsNotEmptyException(catId);
        }
        categoryRepository.deleteById(catId);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        Category category =
                categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId));
        category.setName(categoryDto.getName());
        Category categoryUpdated = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(categoryUpdated);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryRepository.findAll(new OffsetPageRequest(from, size)).map(categoryMapper::toCategoryDto).getContent();
    }

    @Override
    public CategoryDto getCategory(int catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId));
        return categoryMapper.toCategoryDto(category);
    }
}
