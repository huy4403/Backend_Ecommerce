package com.backend_ecommerce.service;

import com.backend_ecommerce.request.CreateProductRequest;
import com.backend_ecommerce.request.ProductHomeRequest;
import com.backend_ecommerce.request.UpdateProductRequest;
import com.backend_ecommerce.response.CreateProductResponse;
import com.backend_ecommerce.response.ProductDisplayResponse;
import com.backend_ecommerce.response.ProductHomeResponse;

public interface ProductService {

    CreateProductResponse createProduct(CreateProductRequest req);

    ProductHomeResponse getAllProduct(ProductHomeRequest req);

    ProductDisplayResponse getProductById(Long id);

    void deleteById(Long id);

    Long updateProduct(Long id, UpdateProductRequest req);

    Long openActiveProduct(Long id);
}
