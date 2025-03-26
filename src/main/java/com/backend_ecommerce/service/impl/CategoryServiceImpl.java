package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.exception.CategoryException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Category;
import com.backend_ecommerce.repository.CategoryRepository;
import com.backend_ecommerce.request.CategoryRequest;
import com.backend_ecommerce.request.CreateAndUpdateCategoryRequest;
import com.backend_ecommerce.response.CategoryDetailsResponse;
import com.backend_ecommerce.response.CategoryResponse;
import com.backend_ecommerce.response.CategoryReviewResponse;
import com.backend_ecommerce.response.CreateAndUpdateCategoryResponse;
import com.backend_ecommerce.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CreateAndUpdateCategoryResponse createCategory(CreateAndUpdateCategoryRequest req) {
        if (categoryRepository.existsByNameIgnoreCase(req.getName()))
            throw new CategoryException("category name already exists");

        Category category = new Category();

        category.setName(req.getName());

        if (req.getParentId() == null)
            category.setLevel(1);
        else {
            Category levelParent = categoryRepository.findById(req.getParentId()).orElseThrow(
                    () -> new ResourceNotFoundException("Parent id not found")
            );
            category.setParentId(req.getParentId());
            category.setLevel(levelParent.getLevel() + 1);
        }
        try {
            return new CreateAndUpdateCategoryResponse(
                    categoryRepository.save(category).getId()
            );
        } catch (Exception e) {
            throw new CategoryException("Something went wrong...");
        }
    }

    @Override
    public CreateAndUpdateCategoryResponse updateCategoryById(Long id, CreateAndUpdateCategoryRequest req) {
        Category existCategory = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + id)
        );

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(req.getName(), id))
            throw new CategoryException("category name already exists");

        existCategory.setName(req.getName());

        if(req.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(req.getParentId()).orElseThrow(
                    () -> new ResourceNotFoundException("Parent id not found")
            );
            existCategory.setParentId(req.getParentId());
            existCategory.setLevel(parentCategory.getLevel() + 1);
        }
        else {
            existCategory.setParentId(null);
            existCategory.setLevel(1);
        }
        try {
            return new CreateAndUpdateCategoryResponse(categoryRepository.save(existCategory).getId());
        } catch (Exception e) {
            throw new CategoryException("Something went wrong...");
        }
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + id)
        );
        try {
            List<Category> childrenCategory = categoryRepository.findByParentId(category.getId());

            if(!childrenCategory.isEmpty()) {

                Long newParentId = category.getParentId();

                for (Category child : childrenCategory) {

                    child.setParentId(newParentId);
                    child.setLevel(Math.max(1, category.getLevel() - 1));
                }

                categoryRepository.saveAll(childrenCategory);
            }

            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new CategoryException("Something went wrong...");
        }
    }

    @Override
    public CategoryReviewResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + id)
        );
        return CategoryReviewResponse
                .builder()
                .name(category.getName())
                .parentId(category.getParentId())
                .build();
    }

    @Override
    public List<CategoryDetailsResponse> getAllCategory() {

        //Get all category
        List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty())
            throw new ResourceNotFoundException("Category not found");

        Map<Long, CategoryDetailsResponse> categoryMap = new HashMap<>();

        for (Category category : categories) {
            categoryMap.put(category.getId(), new CategoryDetailsResponse(
                    category.getId(),
                    category.getName(),
                    category.getLevel()
            ));
        }

        List<CategoryDetailsResponse> rootCategories = new ArrayList<>();

        //Building category tree
        for (Category category : categories) {
            CategoryDetailsResponse categoryResponse = categoryMap.get(category.getId());

            if (category.getParentId() == null) {

                rootCategories.add(categoryResponse);
            } else {

                CategoryDetailsResponse parentCategory = categoryMap.get(category.getParentId());
                if (parentCategory != null) {
                    parentCategory.getChildrenCategories().add(categoryResponse);
                }
            }
        }

        return rootCategories;
    }

    @Override
    public List<CategoryResponse> getAll(CategoryRequest req) {
        List<Category> categories = categoryRepository.findAllAndIdNot(req.getId());
        return categories.stream().map(CategoryResponse::mapFrom).collect(Collectors.toList());
    }
}
