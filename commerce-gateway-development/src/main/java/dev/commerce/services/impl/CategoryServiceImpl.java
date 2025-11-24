package dev.commerce.services.impl;

import dev.commerce.dtos.request.CategoryRequest;
import dev.commerce.dtos.response.CategoryResponse;
import dev.commerce.entitys.Category;
import dev.commerce.mappers.CategoryMapper;
import dev.commerce.repositories.jpa.CategoryRepository;
import dev.commerce.services.CategoryService;
import dev.commerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthenticationUtils utils;

    @Override
    public void deleteCategoryById(UUID categoryId) {
        Category cate = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(String name, int page, int size, String sortBy, String sortDir) {
        Specification<Category> spec = Specification.allOf();
        spec = spec.and((root,query,cr) -> cr.like(root.get("name"), "%" + name + "%"));
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return categoryRepository.findAll(spec,pageable)
                .map(categoryMapper::entityToDto);
    }

    @Override
    public CategoryResponse getCategoryById(UUID categoryId) {
        Category cate = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.entityToDto(cate);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category cate = categoryMapper.dtoToEntity(request);
        cate.setCreatedBy(utils.getCurrentUserId());
        Category savedCate = categoryRepository.save(cate);
        return categoryMapper.entityToDto(savedCate);
    }

    @Override
    public CategoryResponse updateCategory(UUID categoryId, CategoryRequest request) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existingCategory.setName(request.getName());
        existingCategory.setSlug(request.getSlug());
        existingCategory.setUpdatedBy(utils.getCurrentUserId());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.entityToDto(updatedCategory);
    }
}
