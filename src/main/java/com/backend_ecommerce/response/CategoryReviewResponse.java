package com.backend_ecommerce.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryReviewResponse {

    private String name;

    private String parentCategory;
}
