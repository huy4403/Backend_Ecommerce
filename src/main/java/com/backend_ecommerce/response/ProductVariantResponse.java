package com.backend_ecommerce.response;

import com.backend_ecommerce.model.ProductVariant;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductVariantResponse {
    private Long id;

    private List<ProductVariantAttributeValueResponse> attributes;

    private Integer quantity;

    public static ProductVariantResponse mapFromProduct(ProductVariant variants) {

        List<ProductVariantAttributeValueResponse> attributeValues = variants.getAttributeValues().stream()
                .map(variant -> new ProductVariantAttributeValueResponse(variant.getId())
                ).collect(Collectors.toList());

        return ProductVariantResponse
                .builder()
                .id(variants.getId())
                .attributes(attributeValues)
                .quantity(variants.getQuantity())
                .build();
    }
}
