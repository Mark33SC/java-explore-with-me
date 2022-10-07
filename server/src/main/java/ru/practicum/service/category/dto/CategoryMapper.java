package ru.practicum.service.category.dto;

import ru.practicum.service.category.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(CategoryCreateDto createDto) {
        return new Category(null, createDto.getName());
    }
}