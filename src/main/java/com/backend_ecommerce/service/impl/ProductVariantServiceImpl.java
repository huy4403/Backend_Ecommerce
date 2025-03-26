package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.exception.ProductException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.AttributeValue;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.ProductVariant;
import com.backend_ecommerce.repository.ProductRepository;
import com.backend_ecommerce.repository.ProductVariantRepository;
import com.backend_ecommerce.request.AttributeValueRequest;
import com.backend_ecommerce.request.CreateProductVariantRequest;
import com.backend_ecommerce.request.FindProductVariantRequest;
import com.backend_ecommerce.response.VariantResponse;
import com.backend_ecommerce.service.AttributeValueService;
import com.backend_ecommerce.service.ProductVariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final AttributeValueService attributeValueService;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public Long createProductVariant(CreateProductVariantRequest req) {

        //Check product exist ?
        if(!productRepository.existsById(req.getProductId()))
            throw new ResourceNotFoundException("Product with id " + req.getProductId() + " does not exist");

        List<String> attributeValues = req.getAttributeValues().stream()
                .map(AttributeValueRequest::getValue)
                .collect(Collectors.toList());

        if(productVariantRepository.existsByProductAndAttributeValues(req.getProductId(),
                attributeValues,
                attributeValues.size()))
            throw new ProductException("Product variant already exists");

        List<AttributeValue> attributeValuesForProductVariant = req.getAttributeValues().stream()
                .map(attributeValueService::findAndCreateAttributeValue)
                .collect(Collectors.toList());
        try {
            ProductVariant newProductVariant = new ProductVariant();
            newProductVariant.setProduct(Product
                    .builder()
                    .id(req.getProductId())
                    .build());
            newProductVariant.setAttributeValues(attributeValuesForProductVariant);
            newProductVariant.setQuantity(req.getQuantity());
            return productVariantRepository.save(newProductVariant).getId();
        } catch (Exception e) {
            throw new ProductException("Something went wrong...");
        }
    }

    @Override
    public ProductVariant findByAttribute(FindProductVariantRequest req) {

        return productVariantRepository.findByProductIdAndAttributeValueIds(req.getProductId(),
                req.getAttributeIds(),
                req.getAttributeIds().size()).orElseThrow(
                () -> new ResourceNotFoundException("Product variant not exist")
        );
    }

    @Override
    public Long updateProductVariantQuantity(Long id, Integer quantity) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not exist"));

        productVariant.setQuantity(productVariant.getQuantity() + quantity);

        return productVariantRepository.save(productVariant).getId();
    }

    @Override
    public void deleteById(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product variant not exist")
        );

        productVariant.setStatus(ProductStatus.INACTIVE);

        productVariantRepository.save(productVariant);
    }

    @Override
    public List<VariantResponse> getAllVariantById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not exist")
        );
        List<ProductVariant> variants = product.getVariants();
        return variants.stream().map(VariantResponse::mapFromVariant).collect(Collectors.toList());
    }

    @Override
    public void activeProductVariant(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product variant not exist")
        );

        productVariant.setStatus(ProductStatus.ACTIVE);

        productVariantRepository.save(productVariant);
    }
}
