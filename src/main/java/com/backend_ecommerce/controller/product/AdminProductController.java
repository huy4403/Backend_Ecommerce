package com.backend_ecommerce.controller.product;

import com.backend_ecommerce.request.CreateProductRequest;
import com.backend_ecommerce.request.UpdateProductRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.CreateProductResponse;
import com.backend_ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/product")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@ModelAttribute CreateProductRequest req) {
        CreateProductResponse productResponse = productService.createProduct(req);
        return ApiResponse.created("Create product successfully", productResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,
                                           @RequestBody UpdateProductRequest req) {
        Long productId = productService.updateProduct(id, req);
        return ApiResponse.accepted("Update product successfully", productId);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ApiResponse.noContent("Delete product successfully");
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<?> activeProduct(@PathVariable Long id) {
        Long productId = productService.openActiveProduct(id);
        return ApiResponse.accepted("product open active successfully", productId);
    }
}
