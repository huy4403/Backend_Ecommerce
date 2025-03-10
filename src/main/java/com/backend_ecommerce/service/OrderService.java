package com.backend_ecommerce.service;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.PaymentStatus;
import com.backend_ecommerce.request.CreateOrderRequest;
import com.backend_ecommerce.response.CreateOrderResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequest req, HttpServletRequest httpServletRequest);

    void updatePaymentStatus(Long id, PaymentStatus paymentStatus);

    Long updateOrderStatus(Long id, OrderStatus orderStatus);
}
