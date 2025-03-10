package com.backend_ecommerce.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemRequest {
    private Long productVariantId;
    private Integer quantity;
}
