package com.backend_ecommerce.controller.product;

import com.backend_ecommerce.request.ProductManagementRequest;
import com.backend_ecommerce.request.ProductFormRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.CreateProductResponse;
import com.backend_ecommerce.response.ProductFillFormResponse;
import com.backend_ecommerce.response.ProductManagementResponse;
import com.backend_ecommerce.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/product")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@ModelAttribute ProductFormRequest req) {
        CreateProductResponse productResponse = productService.createProduct(req);
        return ApiResponse.created("Create product successfully", productResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,
                                           @ModelAttribute ProductFormRequest req) {
        Long productId = productService.updateProduct(id, req);
        return ApiResponse.accepted("Update product successfully", productId);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ApiResponse.noContent("Delete product successfully");
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<?> deleteProductImage(@PathVariable("id") Long id,
                                                @RequestParam("fileToRemove") @NotBlank(message = "Image not blank") String fileToRemove) {
        productService.deleteProductImage(id, fileToRemove);
        return ApiResponse.noContent("Delete product image successfully");
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<?> activeProduct(@PathVariable Long id) {
        Long productId = productService.openActiveProduct(id);
        return ApiResponse.accepted("product open active successfully", productId);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(@ModelAttribute ProductManagementRequest req) {
        ProductManagementResponse response = productService.getProductManagement(req);
        return ApiResponse.ok("Call api successfully",response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        ProductFillFormResponse product = productService.findProductById(id);
        return ApiResponse.ok("Product overview", product);
    }
}
