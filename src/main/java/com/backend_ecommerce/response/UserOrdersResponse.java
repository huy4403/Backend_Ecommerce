package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserOrdersResponse {
    private Long id;
    private OrderStatus orderStatus;
    private TransactionStatus transactionStatus;
    private int totalItem;
    private Long totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime orderDate;

    public static UserOrdersResponse mapFrom(Order order) {
        return UserOrdersResponse
                .builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .transactionStatus(order.getTransaction().getStatus())
                .totalItem(order.getTotalItem())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .build();
    }
}
