package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.ROLE_NAME;
import com.backend_ecommerce.repository.CategoryRepository;
import com.backend_ecommerce.repository.OrderRepository;
import com.backend_ecommerce.repository.ProductRepository;
import com.backend_ecommerce.repository.UserRepository;
import com.backend_ecommerce.response.DashboardResponse;
import com.backend_ecommerce.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    @Override
    public DashboardResponse getDashBoardOverview() {
        return DashboardResponse
                .builder()
                .customer(userRepository.countUsersByRoleNot(ROLE_NAME.ADMIN))
                .product(productRepository.count())
                .category(categoryRepository.count())
                .order(orderRepository.count())
                .build();
    }
}
