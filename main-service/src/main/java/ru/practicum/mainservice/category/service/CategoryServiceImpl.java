package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.CategoryModel;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.model.EventModel;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.InvalidUniqueKeyException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        try {
            log.info("АДМИН - Создание новой категории {}", newCategoryDto.getName());
            return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.fromNewCategoryDtotoModel(newCategoryDto)));
        } catch (DataIntegrityViolationException exception) {
            throw new InvalidUniqueKeyException("Категория с названием " + newCategoryDto.getName() + " уже существует");
        }
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto categoryDto) {
        CategoryModel updateCategory = getCategoryModelById(catId);
        updateCategory.setName(categoryDto.getName());
        try {
            return CategoryMapper.toCategoryDto(categoryRepository.save(updateCategory));
        } catch (DataIntegrityViolationException exception) {
            throw new InvalidUniqueKeyException("Категория с названием " + categoryDto.getName() + " уже существует");
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена."));
        List<EventModel> eventList = eventRepository.findAllByCategoryId(catId);
        if (!eventList.isEmpty()) {
            throw new ValidationException("Данную категорию нельзя удалить, так как к ней привязаны события");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        log.info("ПАБЛИК - Список категорий");
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.info("ПАБЛИК - Категория с id {}", catId);
        CategoryModel category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryModel getCategoryModelById(Long catId) {
        log.info("ПАБЛИК - Категория с id {}", catId);
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }
}
