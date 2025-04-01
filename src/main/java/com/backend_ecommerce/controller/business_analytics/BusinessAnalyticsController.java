package com.backend_ecommerce.controller.business_analytics;

import com.backend_ecommerce.response.*;
import com.backend_ecommerce.service.BusinessAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/admin")
public class BusinessAnalyticsController {

    private final BusinessAnalyticsService businessAnalyticsService;

    @GetMapping("/business-analytics/summary")
    public ResponseEntity<?> getBusinessOverview(@RequestParam("filter") String filter) {
        BusinessOverviewResponse response = businessAnalyticsService.getOverview(filter);
        return ApiResponse.ok("Overview business analytics", response);
    }

    @GetMapping("/business-analytics/payments")
    public ResponseEntity<?> getPaymentMethod(@RequestParam("filter") String filter) {
        List<BusinessPaymentMethodResponse> response = businessAnalyticsService.analyticsPaymentMethod(filter);
        return ApiResponse.ok("Payment method business analytics", response);
    }

    @GetMapping("/business-analytics/orders")
    public ResponseEntity<?> getOrders(@RequestParam("filter") String filter) {
        List<BusinessOrderResponse> response = businessAnalyticsService.analyticsOrder(filter);
        return ApiResponse.ok("Orders business analytics", response);
    }

    @GetMapping("/business-analytics/products")
    public ResponseEntity<?> getProducts(@RequestParam("filter") String filter) {
        List<BusinessProductResponse> response = businessAnalyticsService.analyticsProduct(filter);
        return ApiResponse.ok("Products business analytics", response);
    }

    @GetMapping("/business-analytics/revenue")
    public ResponseEntity<?> getRevenue(@RequestParam("filter") String filter) {
        List<BusinessRevenueResponse> response = businessAnalyticsService.analyticsRevenue(filter);
        return ApiResponse.ok("Revenue chart", response);
    }

}
