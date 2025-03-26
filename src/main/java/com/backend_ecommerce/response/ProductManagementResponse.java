package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductManagementResponse {
    List<ProductForManagementResponse> products;
    @JsonProperty("total_page")
    private int page;
    @JsonProperty("total_product")
    private Long count;
}
