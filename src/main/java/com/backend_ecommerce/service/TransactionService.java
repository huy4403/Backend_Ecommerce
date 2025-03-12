package com.backend_ecommerce.service;

import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.TransactionStatus;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.User;

public interface TransactionService {
    Long createTranaction(PaymentMethod paymentMethod, User user, Order order);

    void updateStatus(Long orderId, TransactionStatus transactionStatus);
}
