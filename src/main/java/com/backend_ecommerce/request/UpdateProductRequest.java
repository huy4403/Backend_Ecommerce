package com.backend_ecommerce.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProductRequest {
    private String title;

    private Long categoryId;

    private Long importPrice;

    private Long price;

    private String description;
}
