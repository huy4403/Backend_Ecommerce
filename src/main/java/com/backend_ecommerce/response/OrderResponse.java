package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.PaymentStatus;
import com.backend_ecommerce.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;

    private String customer;

    private Long total;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    public static OrderResponse mapFrom(Order order) {
        return OrderResponse
                .builder()
                .id(order.getId())
                .customer(order.getUser().getFullName())
                .total(order.getTotalPrice())
                .paymentStatus(order.getPaymentStatus())
                .orderDate(order.getOrderDate())
                .paymentMethod(order.getTransaction().getPaymentMethod())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
