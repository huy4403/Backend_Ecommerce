package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.exception.AttributeException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Attribute;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.repository.AttributeRepository;
import com.backend_ecommerce.repository.ProductRepository;
import com.backend_ecommerce.request.CreateAttributeRequest;
import com.backend_ecommerce.request.UpdateAttributeRequest;
import com.backend_ecommerce.response.GetAttributeResponse;
import com.backend_ecommerce.service.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;

    @Override
    public Long createProductAttribute(CreateAttributeRequest req) {
        Product product = productRepository.findById(req.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );
        if(attributeRepository.existsByNameIgnoreCaseAndProductId(req.getName(), req.getProductId()))
            throw new AttributeException("Product attribute already exists");

        Attribute attribute = new Attribute();
        attribute.setName(req.getName());
        attribute.setProduct(product);

        try {
            Attribute newAttribute = attributeRepository.save(attribute);
            return newAttribute.getId();
        } catch (Exception e) {
            throw new AttributeException("Something went wrong...");
        }
    }

    @Override
    public Long updateProductAttributeById(Long id, UpdateAttributeRequest req) {
        Attribute existingAttribute = attributeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("product attribute not found")
        );

        if(attributeRepository.existsByNameIgnoreCaseAndProductIdAndIdNot(req.getName(),
                existingAttribute.getProduct().getId(), id)) {
            throw new AttributeException("Product attribute already exists");
        }
        existingAttribute.setName(req.getName());
        try {
            Attribute newAttribute = attributeRepository.save(existingAttribute);
            return newAttribute.getId();
        }
        catch (Exception e) {
            throw new AttributeException("Something went wrong...");
        }
    }

    @Override
    public void deleteProductAttributeById(Long id) {
        Attribute existingAttribute = attributeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("product attribute not found")
        );
        attributeRepository.delete(existingAttribute);
    }

    @Override
    public List<GetAttributeResponse> getAttributeByProductId(Long productId) {

        if(!productRepository.existsById(productId))
            throw new ResourceNotFoundException("product not found");

        List<Attribute> attributes = attributeRepository.findByProductId(productId);
        return attributes.stream()
                .map(GetAttributeResponse::mapFromAttribute)
                .collect(Collectors.toList());
    }
}
