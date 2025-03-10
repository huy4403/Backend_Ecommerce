package com.backend_ecommerce.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDetailsResponse {

    private Long id;
    private String name;
    private Integer level;
    private List<CategoryDetailsResponse> childrenCategories;

    public CategoryDetailsResponse(Long id, String name, Integer level) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.childrenCategories = new ArrayList<>();
    }
}
