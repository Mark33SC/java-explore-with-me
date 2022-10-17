package ru.practicum.service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.category.dto.CategoryDto;
import ru.practicum.service.category.dto.CategoryMapper;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "100") int size
    ) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable long id) {
        return CategoryMapper.toCategoryDto(
                categoryService.getCategoryById(id)
        );
    }
}
