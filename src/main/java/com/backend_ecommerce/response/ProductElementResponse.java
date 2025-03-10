package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProductElementResponse {
    private Long id;
    private String title;
    private Long price;
    private String image;

    public static ProductElementResponse mapFrom(Product product) {

        String productImage = product.getImages().stream().findFirst().orElse(null);

        return ProductElementResponse
                .builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .image(productImage)
                .build();
    }
}
