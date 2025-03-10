package com.backend_ecommerce.controller.category;

import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.CategoryDetailsResponse;
import com.backend_ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/public/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDetailsResponse> categories = categoryService.getAllCategory();
        return ApiResponse.ok("All category", categories);
    }
}
