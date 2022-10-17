package ru.practicum.service.exception.category;

import ru.practicum.service.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String reason) {
        super("Category not found", reason);
    }
}