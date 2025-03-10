package com.backend_ecommerce.response;

import com.backend_ecommerce.model.CartItem;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.ProductVariant;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemResponse {

    private Long id;

    private Long productId;

    private String title;

    private Long price;

    private String productImageUrl;

    private Long productVariantId;

    private List<AttributeValueResponse> attributeValues;

    private Integer stock;

    private Integer quantityBuy;

    public static CartItemResponse mapFromCart(CartItem item) {

        ProductVariant productVariant = item.getProductVariant();

        Product product = productVariant.getProduct();

        String productImageUrl = product.getImages().stream().findFirst().orElse(null);

        return CartItemResponse
                .builder()
                .id(item.getId())
                .productId(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .productImageUrl(productImageUrl)
                .productVariantId(productVariant.getId())
                .attributeValues(productVariant.getAttributeValues().stream().map(
                        AttributeValueResponse::mapFromAttributeValue
                ).collect(Collectors.toList()))
                .stock(productVariant.getQuantity())
                .quantityBuy(item.getQuantity())
                .build();
    }

}
