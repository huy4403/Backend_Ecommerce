package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.exception.AttributeException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Attribute;
import com.backend_ecommerce.model.AttributeValue;
import com.backend_ecommerce.repository.AttributeRepository;
import com.backend_ecommerce.repository.AttributeValueRepository;
import com.backend_ecommerce.request.AttributeValueRequest;
import com.backend_ecommerce.service.AttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttributeValueServiceImpl implements AttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeRepository attributeRepository;

    @Override
    public AttributeValue findAndCreateAttributeValue(AttributeValueRequest req) {

        if(!attributeRepository.existsById(req.getAttributeId())) {
            throw new ResourceNotFoundException("Attribute not found");
        }

        Optional<AttributeValue> attributeValue = attributeValueRepository.findByAttributeIdAndValue(
                req.getAttributeId(),
                req.getValue());

        if(attributeValue.isPresent()) return attributeValue.get();

        AttributeValue newAttributeValue = AttributeValue
                .builder()
                .attribute(new Attribute(req.getAttributeId()))
                .value(req.getValue())
                .build();

        try {
            return attributeValueRepository.save(newAttributeValue);
        } catch (Exception e) {
            throw new AttributeException("Unable to save attribute value");
        }
    }

    @Override
    public void deleteAttributeValue(Long id) {
        AttributeValue attributeValue = attributeValueRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attribute not found"));
        attributeValueRepository.delete(attributeValue);
    }

}
