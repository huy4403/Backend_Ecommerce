package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductNewResponse {
    private Long id;
    private String title;
    private Long price;
    private String image;

    public static ProductNewResponse mapFrom(Product product) {
        return ProductNewResponse
                .builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .image(product.getImages().getFirst())
                .build();
    }
}
