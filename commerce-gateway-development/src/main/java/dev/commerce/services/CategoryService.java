package dev.commerce.services;

import dev.commerce.dtos.request.CategoryRequest;
import dev.commerce.dtos.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CategoryService {
    void deleteCategoryById(UUID categoryId);
    Page<CategoryResponse> getAllCategories(String name, int page, int size, String sortBy, String sortDir);
    CategoryResponse getCategoryById(UUID categoryId);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(UUID categoryId, CategoryRequest request);
}
