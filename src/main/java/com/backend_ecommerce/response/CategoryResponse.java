package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponse {
    private Long id;
    private String name;

    public static CategoryResponse mapFrom(Category category) {
        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
