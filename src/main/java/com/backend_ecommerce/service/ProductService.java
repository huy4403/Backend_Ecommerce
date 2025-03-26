package com.backend_ecommerce.service;

import com.backend_ecommerce.request.ProductHomeRequest;
import com.backend_ecommerce.request.ProductManagementRequest;
import com.backend_ecommerce.request.ProductFormRequest;
import com.backend_ecommerce.response.*;

public interface ProductService {

    CreateProductResponse createProduct(ProductFormRequest req);

    ProductHomeResponse getAllProduct(ProductHomeRequest req);

    ProductDisplayResponse getProductById(Long id);

    void deleteById(Long id);

    Long updateProduct(Long id, ProductFormRequest req);

    Long openActiveProduct(Long id);

    ProductManagementResponse getProductManagement(ProductManagementRequest req);

    ProductFillFormResponse findProductById(Long id);
}
