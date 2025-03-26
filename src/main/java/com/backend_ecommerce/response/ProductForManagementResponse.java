package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.ProductVariant;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductForManagementResponse {
    private Long id;
    private String title;
    private Long price;
    private Integer stock;
    private String category;
    private String brand;
    private ProductStatus status;
    private Integer variant;
    private String image;

    public static ProductForManagementResponse mapFrom(Product product) {

        Integer stock = product.getVariants().stream()
                .mapToInt(ProductVariant::getQuantity)
                .sum();

        return ProductForManagementResponse
                .builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .stock(stock)
                .category(product.getCategory().getName())
                .brand(product.getBrand())
                .image(product.getImages().stream().findFirst().orElse(null))
                .status(product.getStatus())
                .variant(product.getVariants().size())
                .build();
    }

}
