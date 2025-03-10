package com.backend_ecommerce.service;

import com.backend_ecommerce.model.AttributeValue;
import com.backend_ecommerce.request.AttributeValueRequest;

public interface AttributeValueService {

    AttributeValue findAndCreateAttributeValue(AttributeValueRequest req);

    void deleteAttributeValue(Long id);
}
