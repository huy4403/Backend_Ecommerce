package com.backend_ecommerce.controller.order;

import com.backend_ecommerce.request.CreateOrderRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.CreateOrderResponse;
import com.backend_ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
