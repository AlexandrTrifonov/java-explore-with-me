package ru.practicum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.constants.Constants;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CategoryPublicController {
    final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive int size) {
        log.info("ПАБЛИК - Запрос на получение списка категорий");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("ПАБЛИК - Запрос на получение категории по id {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
