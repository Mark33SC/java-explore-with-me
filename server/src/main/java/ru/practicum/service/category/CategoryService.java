package ru.practicum.service.category;

import ru.practicum.service.category.dto.CategoryCreateDto;
import ru.practicum.service.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryCreateDto createDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(long id);

    List<CategoryDto> getAllCategories(int from, int size);

    Category getCategoryById(long id);

}
