package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.model.Order;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BusinessRevenueResponse {
    private String name;
    private Long value;

    public static List<BusinessRevenueResponse> mapFrom(List<Order> orders, String filter) {
        Map<String, Long> revenueMap = new LinkedHashMap<>();
        DateTimeFormatter formatter;
        List<String> timeSlots = new ArrayList<>();

        switch (filter) {
            case "weekly":
                formatter = DateTimeFormatter.ofPattern("EEE");
                timeSlots = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
                break;
            case "daily":
                formatter = DateTimeFormatter.ofPattern("HH");
                for (int i = 0; i < 24; i++) timeSlots.add(String.format("%02d", i));
                break;
            case "monthly":
                formatter = DateTimeFormatter.ofPattern("dd");
                for (int i = 1; i <= 30; i++) timeSlots.add(String.format("%02d", i));
                break;
            case "yearly":
                formatter = DateTimeFormatter.ofPattern("MM");
                for (int i = 1; i <= 12; i++) timeSlots.add(String.format("%02d", i));
                break;
            default:
                throw new IllegalArgumentException("Invalid filter type");
        }

        for (String slot : timeSlots) {
            revenueMap.put(slot, 0L);
        }

        for (Order order : orders) {
            if (!order.getTransaction().getStatus().equals(TransactionStatus.FAILED)) {
                String key = order.getOrderDate().format(formatter);
                revenueMap.put(key, revenueMap.getOrDefault(key, 0L) + order.getTotalPrice());
            }
        }

        return revenueMap.entrySet().stream()
                .map(entry -> new BusinessRevenueResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
