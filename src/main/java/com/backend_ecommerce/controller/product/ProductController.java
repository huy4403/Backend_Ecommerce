package com.backend_ecommerce.controller.product;

import com.backend_ecommerce.request.ProductHomeRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.ProductDisplayResponse;
import com.backend_ecommerce.response.ProductHomeResponse;
import com.backend_ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/public/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProduct(@ModelAttribute ProductHomeRequest request) {
        ProductHomeResponse products = productService.getAllProduct(request);
        return ApiResponse.ok("All products", products);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        ProductDisplayResponse product = productService.getProductById(id);
        return ApiResponse.ok("Product details", product);
    }
}
