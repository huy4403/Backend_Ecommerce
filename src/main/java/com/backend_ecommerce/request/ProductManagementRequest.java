package com.backend_ecommerce.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductManagementRequest {
    @Builder.Default
    private String title = "";
    private Integer minPrice;
    private Integer maxPrice;
    @Builder.Default
    private String category = "";
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer limit = 30;
}
