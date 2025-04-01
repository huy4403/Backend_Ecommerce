package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.Review;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDisplayResponse {
    private Long id;
    private String title;
    private double rating;
    private long reviews;
    private String description;
    private Long price;
    private String brand;
    private List<String> images;
    private List<AttributeResponse> attributes;
    private List<ProductVariantResponse> variants;
    @Builder.Default
    private boolean bought = false;

    public static ProductDisplayResponse mapFromProduct(Product product) {

        ProductDisplayResponse productDisplayResponse = new ProductDisplayResponse();
        productDisplayResponse.setId(product.getId());
        productDisplayResponse.setTitle(product.getTitle());
        productDisplayResponse.setDescription(product.getDescription());
        productDisplayResponse.setPrice(product.getPrice());
        productDisplayResponse.setImages(product.getImages());
        productDisplayResponse.setBrand(product.getBrand());

        productDisplayResponse.setAttributes(
                product.getAttributes().stream().map(AttributeResponse::mapFromAttribute).collect(Collectors.toList())
        );

        productDisplayResponse.setRating(
                product.getReviews().stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0)
        );

        productDisplayResponse.setReviews(product.getReviews() != null ? (long) product.getReviews().size() : 0L);

        productDisplayResponse.setVariants(product.getVariants().stream()
                .filter(variant -> variant.getStatus() == ProductStatus.ACTIVE)
                .map(ProductVariantResponse::mapFromProduct)
                .collect(Collectors.toList()));

        return productDisplayResponse;
    }
}
