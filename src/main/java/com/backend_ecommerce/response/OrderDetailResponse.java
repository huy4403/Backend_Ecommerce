package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.dto.OrderItemDTO;
import com.backend_ecommerce.model.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private Long id;
    private String customer;
    private AddressResponse shippingAddress;
    private PaymentMethod paymentMethod;
    private int totalItem;
    private Long totalPrice;
    private TransactionStatus transactionStatus;
    private List<OrderItemDTO> items;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime deliveryDate;

    public static OrderDetailResponse mapFrom(Order order) {
        return OrderDetailResponse
                .builder()
                .id(order.getId())
                .customer(order.getUser().getFullName())
                .shippingAddress(AddressResponse.mapFromAddress(order.getShippingAddress()))
                .paymentMethod(order.getTransaction().getPaymentMethod())
                .totalItem(order.getTotalItem())
                .totalPrice(order.getTotalPrice())
                .transactionStatus(order.getTransaction().getStatus())
                .items(order.getOrderItems().stream().map(OrderItemDTO::mapFrom).collect(Collectors.toList()))
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .deliveryDate(order.getDeliverDate())
                .build();
    }
}
