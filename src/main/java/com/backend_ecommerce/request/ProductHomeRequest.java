package com.backend_ecommerce.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductHomeRequest {
    @Builder.Default
    private String title = "";
    @Builder.Default
    private String categoryName = "";
    private Integer minPrice;
    private Integer maxPrice;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer limit = 9;
}
