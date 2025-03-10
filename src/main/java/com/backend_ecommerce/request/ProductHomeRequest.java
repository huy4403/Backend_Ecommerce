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
    private Long categoryId = null;
    @Builder.Default
    private Integer minPrice = 0;
    @Builder.Default
    private Integer maxPrice = 0;
    @Builder.Default
    private Integer page = 0  ;
    @Builder.Default
    private Integer limit = 9;
}
