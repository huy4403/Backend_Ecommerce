package com.backend_ecommerce.service;

import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.PaymentOrderStatus;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.User;

public interface PaymentOrderService {
    Long createPaymentOrder(PaymentMethod paymentMethod, User user, Order order);

    void updateStatus(Long orderId, PaymentOrderStatus paymentOrderStatus);
}
