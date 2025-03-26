package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.dto.AttributeValueDTO;
import com.backend_ecommerce.model.ProductVariant;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VariantResponse {
    private Long id;
    private List<AttributeValueDTO> attributes;
    private Integer quantity;
    private ProductStatus status;

    public static VariantResponse mapFromVariant(ProductVariant variant) {
        return VariantResponse
                .builder()
                .id(variant.getId())
                .attributes(variant.getAttributeValues().stream().map(AttributeValueDTO::mapFrom).collect(Collectors.toList()))
                .quantity(variant.getQuantity())
                .status(variant.getStatus())
                .build();
    }
}
