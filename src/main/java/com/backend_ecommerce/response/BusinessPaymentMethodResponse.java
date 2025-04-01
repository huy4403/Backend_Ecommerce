package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.Transaction;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BusinessPaymentMethodResponse {
    private PaymentMethod method;
    private Long value;
    private int percentage;

    public static List<BusinessPaymentMethodResponse> mapFrom(List<Order> orders) {
        List<Transaction> transactions = orders.stream()
                .map(Order::getTransaction)
                .filter(t -> !t.getStatus().equals(TransactionStatus.FAILED))
                .toList();

        long totalValue = transactions.stream().mapToLong(Transaction::getAmount).sum();

        Map<PaymentMethod, Long> methodTotals = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentMethod, Collectors.summingLong(Transaction::getAmount)));

        return methodTotals.entrySet().stream()
                .map(entry -> new BusinessPaymentMethodResponse(
                        entry.getKey(),
                        entry.getValue(),
                        totalValue == 0 ? 0 : (int) ((entry.getValue() * 100) / totalValue)
                ))
                .collect(Collectors.toList());
    }
}
