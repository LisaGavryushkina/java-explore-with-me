package ru.practicum.ewm.category;

import java.util.List;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.log.Logged;
import ru.practicum.ewm.pageable.OffsetPageRequest;

@Service
@RequiredArgsConstructor
@Logged
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.toCategory(categoryDto));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto deleteCategory(int catId) {
        Category category = categoryRepository.findByIdOrThrow(catId);
        List<Event> categoryEvents = eventRepository.findAllByCategoryId(catId);
        if (!categoryEvents.isEmpty()) {
            throw new CategoryIsNotEmptyException(catId);
        }
        categoryRepository.deleteById(catId);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findByIdOrThrow(catId);
        category.setName(categoryDto.getName());
        Category categoryUpdated = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(categoryUpdated);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryRepository.findAll(new OffsetPageRequest(from, size, Sort.unsorted())).map(categoryMapper::toCategoryDto).getContent();
    }

    @Override
    public CategoryDto getCategory(int catId) {
        Category category = categoryRepository.findByIdOrThrow(catId);
        return categoryMapper.toCategoryDto(category);
    }
}
