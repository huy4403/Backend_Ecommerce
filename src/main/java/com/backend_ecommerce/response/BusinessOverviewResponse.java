package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.model.Order;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BusinessOverviewResponse {
    private Long revenue;
    private Long profit;
    private Long totalOrders;
    private Long delivered;
    private Long cancelled;

    public static BusinessOverviewResponse mapFrom(List<Order> orders) {

        Long revenue = orders.stream()
                .mapToLong(Order::getTotalPrice)
                .reduce(0L, Long::sum);

        Long cost = orders.stream()
                .flatMapToLong(order -> order.getOrderItems().stream()
                        .mapToLong(orderItem -> orderItem.getImportPrice() * orderItem.getQuantity()))
                .reduce(0L, Long::sum);

        Long totalOrders = (long) orders.size();

        Long delivered = orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED)
                .count();

        Long cancelled = orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.CANCELLED)
                .count();

        return BusinessOverviewResponse
                .builder()
                .revenue(revenue)
                .profit(revenue - cost)
                .totalOrders(totalOrders)
                .delivered(delivered)
                .cancelled(cancelled)
                .build();
    }
}
