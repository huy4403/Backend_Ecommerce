package com.backend_ecommerce.service;

import com.backend_ecommerce.model.ProductVariant;
import com.backend_ecommerce.request.CreateProductVariantRequest;
import com.backend_ecommerce.request.FindProductVariantRequest;

public interface ProductVariantService {

    Long createProductVariant(CreateProductVariantRequest req);

    ProductVariant findByAttribute(FindProductVariantRequest req);

    Long updateProductVariantQuantity(Long id, Integer quantity);

    void deleteById(Long id);
}
