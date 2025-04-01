package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.repository.OrderRepository;
import com.backend_ecommerce.response.*;
import com.backend_ecommerce.service.BusinessAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessAnalyticsServiceImpl implements BusinessAnalyticsService {
    private final OrderRepository orderRepository;

    @Override
    public BusinessOverviewResponse getOverview(String filter) {

        DateRange dateRange = calDate(filter);

        List<Order> orders = orderRepository.getByOrderDate(dateRange.startDate(), dateRange.endDate());

        return BusinessOverviewResponse.mapFrom(orders);
    }

    @Override
    public List<BusinessPaymentMethodResponse> analyticsPaymentMethod(String filter) {
        DateRange dateRange = calDate(filter);
        List<Order> orders = orderRepository.getByOrderDate(dateRange.startDate, dateRange.endDate);

        return BusinessPaymentMethodResponse.mapFrom(orders);
    }

    @Override
    public List<BusinessOrderResponse> analyticsOrder(String filter) {
        DateRange dateRange = calDate(filter);
        List<Order> orders = orderRepository.getByOrderDate(dateRange.startDate, dateRange.endDate);
        return BusinessOrderResponse.mapFrom(orders);
    }

    @Override
    public List<BusinessProductResponse> analyticsProduct(String filter) {
        DateRange dateRange = calDate(filter);
        List<Order> orders = orderRepository.getByOrderDate(dateRange.startDate, dateRange.endDate);
        return BusinessProductResponse.mapFrom(orders);
    }

    @Override
    public List<BusinessRevenueResponse> analyticsRevenue(String filter) {
        DateRange dateRange = calDate(filter);
        List<Order> orders = orderRepository.getByOrderDate(dateRange.startDate, dateRange.endDate);
        return BusinessRevenueResponse.mapFrom(orders, filter);
    }

    private DateRange calDate(String filter) {
        LocalDateTime startDate;
        LocalDateTime.now();
        LocalDateTime endDate = switch (filter.toLowerCase()) {
            case "daily" -> {
                startDate = LocalDate.now().atStartOfDay();
                yield startDate.plusDays(1);
            }
            case "weekly" -> {
                startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
                yield startDate.plusWeeks(1);
            }
            case "monthly" -> {
                startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                yield startDate.plusMonths(1);
            }
            case "yearly" -> {
                startDate = LocalDate.now().withDayOfYear(1).atStartOfDay();
                yield startDate.plusYears(1);
            }
            default ->
                    throw new IllegalArgumentException("Filter không hợp lệ. Chỉ chấp nhận: daily, weekly, monthly, yearly.");
        };

        return new DateRange(startDate, endDate);
    }

    public record DateRange(LocalDateTime startDate, LocalDateTime endDate) {
    }
}

