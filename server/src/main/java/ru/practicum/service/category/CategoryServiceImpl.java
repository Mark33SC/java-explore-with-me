package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.service.category.dto.CategoryCreateDto;
import ru.practicum.service.category.dto.CategoryDto;
import ru.practicum.service.category.dto.CategoryMapper;
import ru.practicum.service.exception.category.CategoryNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto addCategory(CategoryCreateDto createDto) {
        Category category = CategoryMapper.toCategory(createDto);
        categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = getCategoryById(categoryDto.getId());

        category.setName(categoryDto.getName());

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);

        List<Category> categories = categoryRepository.findAll(pageRequest).toList();

        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(String.format("Category with id:%s not found", id))
        );
    }
}
