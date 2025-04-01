package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.model.Order;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BusinessOrderResponse {
    private OrderStatus status;
    private Long value;
    private int percentage;

    public static List<BusinessOrderResponse> mapFrom(List<Order> orders) {
        Map<OrderStatus, Long> orderCountMap = Arrays.stream(OrderStatus.values())
                .collect(Collectors.toMap(status -> status, status -> 0L));

        orders.forEach(order ->
                orderCountMap.put(order.getOrderStatus(), orderCountMap.get(order.getOrderStatus()) + 1)
        );

        long totalOrders = orders.size();

        return orderCountMap.entrySet().stream()
                .map(entry -> BusinessOrderResponse.builder()
                        .status(entry.getKey())
                        .value(entry.getValue())
                        .percentage(totalOrders > 0 ? (int) ((entry.getValue() * 100) / totalOrders) : 0)
                        .build())
                .collect(Collectors.toList());
    }
}
