package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.ProductVariant;
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
    private Integer stock;
    private String brand;
    private String image;

    public static ProductElementResponse mapFrom(Product product) {

        Integer stock = product.getVariants().stream()
                .mapToInt(ProductVariant::getQuantity)
                .sum();

        return ProductElementResponse
                .builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .stock(stock)
                .brand(product.getBrand())
                .image(product.getImages().stream().findFirst().orElse(null))
                .build();
    }
}
