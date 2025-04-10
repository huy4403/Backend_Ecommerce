package com.backend_ecommerce.controller.order;

import com.backend_ecommerce.request.CreateOrderRequest;
import com.backend_ecommerce.response.*;
import com.backend_ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest req) {
        CreateOrderResponse orderResponse = orderService.createOrder(request, req);
        return ApiResponse.ok("Create order successfully", orderResponse);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> rePayment(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        RePaymentResponse rePaymentResponse = orderService.rePayment(id, httpServletRequest);
        return ApiResponse.ok("Re payment successfully", rePaymentResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        OrderDetailResponse response = orderService.getCurrentOrderById(id);
        return ApiResponse.ok("Get order successfully", response);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<UserOrdersResponse> response = orderService.getUserOrders();
        return ApiResponse.ok("Get orders successfully", response);
    }
}
