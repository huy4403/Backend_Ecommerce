package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.PaymentOrderStatus;
import com.backend_ecommerce.exception.PaymentException;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.PaymentOrder;
import com.backend_ecommerce.model.User;
import com.backend_ecommerce.repository.PaymentOrderRepository;
import com.backend_ecommerce.service.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    public Long createPaymentOrder(PaymentMethod paymentMethod, User user, Order order) {

        PaymentOrder paymentOrder = PaymentOrder
                .builder()
                .paymentMethod(paymentMethod)
                .user(user)
                .order(order)
                .build();
        return paymentOrderRepository.save(paymentOrder).getId();
    }

    @Override
    public void updateStatus(Long orderId, PaymentOrderStatus paymentOrderStatus) {

        PaymentOrder paymentOrder = paymentOrderRepository.findByOrderId(orderId).orElseThrow(
                () -> new PaymentException("Payment order not found")
        );

        paymentOrder.setStatus(paymentOrderStatus);

        paymentOrderRepository.save(paymentOrder);

    }
}
