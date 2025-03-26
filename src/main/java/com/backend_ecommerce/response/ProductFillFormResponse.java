package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.model.Product;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductFillFormResponse {
    private String title;

    private Long categoryId;

    private Long importPrice;

    private Long price;

    private String description;

    private String brand;

    private List<String> images;

    private ProductStatus status;

    public static ProductFillFormResponse mapFrom(Product product) {
        return ProductFillFormResponse
                .builder()
                .title(product.getTitle())
                .categoryId(product.getCategory().getId())
                .importPrice(product.getImportPrice())
                .price(product.getPrice())
                .description(product.getDescription())
                .brand(product.getBrand())
                .images(product.getImages())
                .status(product.getStatus())
                .build();
    }
}
