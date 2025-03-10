package com.backend_ecommerce.controller.product;

import com.backend_ecommerce.request.CreateAttributeRequest;
import com.backend_ecommerce.request.UpdateAttributeRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.GetAttributeResponse;
import com.backend_ecommerce.service.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/product-attribute")
public class ProductAttributeController {

    private final AttributeService attributeService;

    @PostMapping
    public ResponseEntity<?> createProductAttribute(@RequestBody CreateAttributeRequest req){
        Long attributeId = attributeService.createProductAttribute(req);
        return ApiResponse.created("Product attribute created successfully", attributeId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductAttribute(@PathVariable("id") Long id,
                                                    @RequestBody UpdateAttributeRequest req){
        Long attributeId = attributeService.updateProductAttributeById(id, req);
        return ApiResponse.accepted("Product attribute updated successfully", attributeId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductAttribute(@PathVariable("id") Long id){
        attributeService.deleteProductAttributeById(id);
        return ApiResponse.noContent("Product attribute delete successfully");
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllProductAttribute(@RequestParam("productId") Long productId){
        List<GetAttributeResponse> attributes = attributeService.getAttributeByProductId(productId);
        return ApiResponse.ok("Product attribute list", attributes);
    }

}
