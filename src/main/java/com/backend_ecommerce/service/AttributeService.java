package com.backend_ecommerce.service;

import com.backend_ecommerce.request.CreateAttributeRequest;
import com.backend_ecommerce.request.UpdateAttributeRequest;
import com.backend_ecommerce.response.GetAttributeResponse;

import java.util.List;

public interface AttributeService {

    Long createProductAttribute(CreateAttributeRequest req);

    Long updateProductAttributeById(Long id, UpdateAttributeRequest req);

    void deleteProductAttributeById(Long id);

    List<GetAttributeResponse> getAttributeByProductId(Long productId);
}
