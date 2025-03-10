package com.backend_ecommerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAndUpdateCategoryRequest {

    @NotBlank(message = "Category name doesn't blank")
    private String name;

    private Long parentId;
}
