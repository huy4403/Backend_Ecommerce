package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.OrderItem;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.ProductVariant;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BusinessProductResponse {

    private String image;
    private String title;
    private Long sales;
    private Long revenue;
    private Long profit;
    private Long stock;

    public static List<BusinessProductResponse> mapFrom(List<Order> orders) {
        Map<String, BusinessProductResponse> productMap = new HashMap<>();

        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                String productKey = product.getTitle();

                BusinessProductResponse response = productMap.getOrDefault(productKey,
                        BusinessProductResponse.builder()
                                .image(product.getImages().getFirst())
                                .title(product.getTitle())
                                .sales(0L)
                                .revenue(0L)
                                .profit(0L)
                                .stock(0L)
                                .build()
                );

                response.setSales(response.getSales() + orderItem.getQuantity());

                Long revenue = orderItem.getPrice() * orderItem.getQuantity();

                Long cost = orderItem.getImportPrice() * orderItem.getQuantity();

                response.setRevenue(response.getRevenue() + revenue);

                response.setProfit(response.getProfit() + (revenue-cost));

                Long totalStock = product.getVariants().stream()
                        .mapToLong(ProductVariant::getQuantity)
                        .sum();
                response.setStock(totalStock);

                productMap.put(productKey, response);
            }
        }
        return new ArrayList<>(productMap.values());
    }
}
