package com.backend_ecommerce.service;

import com.backend_ecommerce.request.CreateAndUpdateCategoryRequest;
import com.backend_ecommerce.response.CategoryDetailsResponse;
import com.backend_ecommerce.response.CategoryReviewResponse;
import com.backend_ecommerce.response.CreateAndUpdateCategoryResponse;

import java.util.List;

public interface CategoryService {
    CreateAndUpdateCategoryResponse createCategory(CreateAndUpdateCategoryRequest req);

    CreateAndUpdateCategoryResponse updateCategoryById(Long id, CreateAndUpdateCategoryRequest req);

    void deleteCategoryById(Long id);

    CategoryReviewResponse getCategoryById(Long id);

    List<CategoryDetailsResponse> getAllCategory();
}
