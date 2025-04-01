package com.backend_ecommerce.dto;
import com.backend_ecommerce.model.OrderItem;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDTO {
    private Long productId;
    private String image;
    private String title;
    private List<AttributeValueDTO> attribute;
    private String brand;
    private int quantity;
    private Long price;

    public static OrderItemDTO mapFrom(OrderItem orderItem) {
        return OrderItemDTO
                .builder()
                .productId(orderItem.getProduct().getId())
                .image(orderItem.getProduct().getImages().getFirst())
                .title(orderItem.getProduct().getTitle())
                .attribute(orderItem.getProductVariant()
                        .getAttributeValues()
                        .stream().map(AttributeValueDTO::mapFrom)
                        .collect(Collectors.toList()))
                .brand(orderItem.getProduct().getBrand())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
