package com.backend_ecommerce.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity must be not null")
    private Integer quantity;
}
