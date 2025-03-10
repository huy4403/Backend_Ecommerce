package com.backend_ecommerce.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateProductVariantRequest {
    private Long productId;
    private List<AttributeValueRequest> attributeValues;
    private Integer quantity;
}
