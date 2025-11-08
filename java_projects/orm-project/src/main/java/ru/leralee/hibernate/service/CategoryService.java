package ru.leralee.hibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leralee.hibernate.dto.request.CategoryRequest;
import ru.leralee.hibernate.dto.response.CategoryResponse;
import ru.leralee.hibernate.entity.Category;
import ru.leralee.hibernate.exception.AppException;
import ru.leralee.hibernate.exception.NotFoundException;
import ru.leralee.hibernate.mapper.CategoryMapper;
import ru.leralee.hibernate.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author valeriali
 * @project orm-project
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        categoryRepository.findBySlug(request.getSlug()).ifPresent(category -> {
            throw new AppException("Category with slug '" + request.getSlug() + "' already exists", 409);
        });

        Category category = categoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(UUID id) {
        return categoryMapper.toResponse(getCategory(id));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }

    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category category = getCategory(id);
        categoryMapper.updateEntityFromRequest(request, category);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(UUID id) {
        Category category = getCategory(id);
        if (!category.getCourses().isEmpty()) {
            throw new AppException("Cannot delete category with active courses", 409);
        }
        categoryRepository.deleteById(id);
    }

    private Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
    }
}
