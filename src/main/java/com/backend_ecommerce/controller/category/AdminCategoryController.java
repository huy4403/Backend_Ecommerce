package com.backend_ecommerce.controller.category;

import com.backend_ecommerce.request.CategoryRequest;
import com.backend_ecommerce.request.CreateAndUpdateCategoryRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.CategoryResponse;
import com.backend_ecommerce.response.CategoryReviewResponse;
import com.backend_ecommerce.response.CreateAndUpdateCategoryResponse;
import com.backend_ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/category")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CreateAndUpdateCategoryRequest req) {
        CreateAndUpdateCategoryResponse category = categoryService.createCategory(req);
        return ApiResponse.created("Create category successfully", category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryById(@PathVariable("id") Long id,
                                                @RequestBody CreateAndUpdateCategoryRequest req) {
        CreateAndUpdateCategoryResponse category = categoryService.updateCategoryById(id, req);
        return ApiResponse.accepted("Update category successfully", category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return ApiResponse.noContent("Delete category successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        CategoryReviewResponse categoryReviewResponse = categoryService.getCategoryById(id);
        return ApiResponse.ok("Category details", categoryReviewResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories(@ModelAttribute CategoryRequest req) {
        List<CategoryResponse> categories = categoryService.getAll(req);
        return ApiResponse.ok("Category details", categories);
    }
}
