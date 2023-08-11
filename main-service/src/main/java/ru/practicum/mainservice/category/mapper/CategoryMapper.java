package ru.practicum.mainservice.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.model.CategoryModel;

@UtilityClass
public class CategoryMapper {

    public CategoryDto toCategoryDto(CategoryModel categoryModel) {
        return CategoryDto.builder()
                .id(categoryModel.getId())
                .name(categoryModel.getName())
                .build();
    }

    public CategoryModel fromNewCategoryDtotoModel(NewCategoryDto newCategoryDto) {
        return CategoryModel.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryModel fromCategoryDtotoModel(CategoryDto categoryDto) {
        return CategoryModel.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}
