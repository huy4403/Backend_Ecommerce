package com.backend_ecommerce.controller.order;

import com.backend_ecommerce.request.UpdateOrderStatusRequest;
import com.backend_ecommerce.request.UpdateTransactionStatusRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.OrderDetailResponse;
import com.backend_ecommerce.response.OrderResponse;
import com.backend_ecommerce.service.OrderService;
import com.backend_ecommerce.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/order")
public class AdminOrderController {
    private final OrderService orderService;
    private final TransactionService transactionService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("id") Long id, @RequestBody UpdateOrderStatusRequest req) {
        Long orderId = orderService.updateOrderStatus(id, req.getOrderStatus());
        return ApiResponse.accepted("Update order status successfully", orderId);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrder() {
        List<OrderResponse> order = orderService.getAll();
        return ApiResponse.ok("Get order status", order);
    }

    @PutMapping("/{id}/transaction")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable("id") Long id, @RequestBody UpdateTransactionStatusRequest req) {
        transactionService.updateStatus(id, req.getStatus());
        return ApiResponse.accepted("Update payment status successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        OrderDetailResponse response = orderService.getOrderById(id);
        return ApiResponse.ok("Get order status successfully", response);
    }

}
