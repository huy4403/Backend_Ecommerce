package com.backend_ecommerce.controller.order;

import com.backend_ecommerce.request.UpdateOrderStatusRequest;
import com.backend_ecommerce.request.UpdatePaymentStatusRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.OrderResponse;
import com.backend_ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/order")
public class AdminOrderController {
    private final OrderService orderService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest req) {
        Long orderId = orderService.updateOrderStatus(id, req.getOrderStatus());
        return ApiResponse.accepted("Update order status successfully", orderId);
    }

    @GetMapping
    public ResponseEntity<?> getOrderStatus() {
        List<OrderResponse> order = orderService.getAll();
        return ApiResponse.ok("Get order status", order);
    }

//    @GetMapping("{id}/payment")
//    public ResponseEntity<?> updatePaymentStatus(@PathVariable("id") Long id, @RequestBody UpdatePaymentStatusRequest req) {
//        Long id = orderService
//    }
}
