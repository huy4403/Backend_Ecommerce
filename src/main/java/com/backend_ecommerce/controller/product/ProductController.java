package com.backend_ecommerce.controller.product;

import com.backend_ecommerce.request.ProductHomeRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.ProductDisplayResponse;
import com.backend_ecommerce.response.ProductHomeResponse;
import com.backend_ecommerce.response.ReviewResponse;
import com.backend_ecommerce.service.ProductService;
import com.backend_ecommerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/public/product")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

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

    @GetMapping("{id}/reviews")
    public ResponseEntity<?> getProductReviews(@PathVariable("id") Long id,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "limit", defaultValue = "9") int limit) {
        List<ReviewResponse> reviewResponses = reviewService.getReviews(id, page, limit);
        return ApiResponse.ok("All reviews", reviewResponses);
    }
}
