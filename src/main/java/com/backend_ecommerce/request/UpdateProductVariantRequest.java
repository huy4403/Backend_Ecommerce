package com.backend_ecommerce.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProductVariantRequest {

    @NotNull(message = "Quantity must not be null")
    private Integer quantity;
}
