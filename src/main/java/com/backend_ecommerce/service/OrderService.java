package com.backend_ecommerce.service;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.request.CreateOrderRequest;
import com.backend_ecommerce.response.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequest req, HttpServletRequest httpServletRequest);

    Long updateOrderStatus(Long id, OrderStatus orderStatus);

    List<OrderResponse> getAll();

    OrderDetailResponse getOrderById(Long id);

    OrderDetailResponse getCurrentOrderById(Long id);

    List<UserOrdersResponse> getUserOrders();

    RePaymentResponse rePayment(Long id, HttpServletRequest httpServletRequest);
}
