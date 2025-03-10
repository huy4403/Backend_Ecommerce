package com.backend_ecommerce.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductHomeResponse {
    private List<ProductElementResponse> products;
    @JsonProperty("total_page")
    private int page;
    @JsonProperty("total_product")
    private Long count;
}
