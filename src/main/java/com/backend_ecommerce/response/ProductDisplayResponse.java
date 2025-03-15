package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Product;
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
    private String description;
    private Long price;
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

        productDisplayResponse.setAttributes(
                product.getAttributes().stream().map(AttributeResponse::mapFromAttribute).collect(Collectors.toList())
        );

        productDisplayResponse.setVariants(product.getVariants().stream().map(ProductVariantResponse::mapFromProduct)
                .collect(Collectors.toList()));

        return productDisplayResponse;
    }
}
