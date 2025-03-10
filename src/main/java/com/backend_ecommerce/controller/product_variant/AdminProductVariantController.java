package com.backend_ecommerce.controller.product_variant;

import com.backend_ecommerce.model.ProductVariant;
import com.backend_ecommerce.request.CreateProductVariantRequest;
import com.backend_ecommerce.request.FindProductVariantRequest;
import com.backend_ecommerce.request.UpdateProductVariantRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/product-variant")
public class AdminProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<?> createProductVariant(@RequestBody CreateProductVariantRequest req) {
        Long productVariantId = productVariantService.createProductVariant(req);
        return ApiResponse.created("Successfully created a product variant.", productVariantId);
    }

    @GetMapping
    public ResponseEntity<?> getProductVariant(@RequestBody FindProductVariantRequest req) {
        ProductVariant productVariant = productVariantService.findByAttribute(req);
        return ApiResponse.ok("Product variant", productVariant);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateQuantityProductVariant(@PathVariable("id") Long id,
                                                          @Valid @RequestBody UpdateProductVariantRequest req) {
        Long productVariantId = productVariantService.updateProductVariantQuantity(id, req.getQuantity());
        return ApiResponse.accepted("Product variant updated", productVariantId);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProductVariant(@PathVariable("id") Long id) {
        productVariantService.deleteById(id);
        return ApiResponse.noContent("Success to delete");
    }
}
