package com.backend_ecommerce.service;

import com.backend_ecommerce.response.*;

import java.util.List;

public interface BusinessAnalyticsService {
    BusinessOverviewResponse getOverview(String filter);

    List<BusinessPaymentMethodResponse> analyticsPaymentMethod(String filter);

    List<BusinessOrderResponse> analyticsOrder(String filter);

    List<BusinessProductResponse> analyticsProduct(String filter);

    List<BusinessRevenueResponse> analyticsRevenue(String filter);
}
