package com.backend_ecommerce.request;

import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "Quantity must be greater than 1")
    @NotNull(message = "Quantity must not be null")
    private Integer quantity;
}
