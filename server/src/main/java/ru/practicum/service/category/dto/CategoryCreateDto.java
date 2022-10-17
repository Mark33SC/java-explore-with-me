package ru.practicum.service.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryCreateDto {
    @NotBlank
    private String name;
}