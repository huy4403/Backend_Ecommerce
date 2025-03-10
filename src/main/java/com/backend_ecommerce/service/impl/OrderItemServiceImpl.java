package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.exception.OrderException;
import com.backend_ecommerce.model.CartItem;
import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.OrderItem;
import com.backend_ecommerce.repository.CartItemRepository;
import com.backend_ecommerce.repository.OrderItemRepository;
import com.backend_ecommerce.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Long addOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(cartItem.getProductVariant().getProduct());
        orderItem.setProductVariant(cartItem.getProductVariant());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getProductVariant().getProduct().getPrice());

        try {
            Long orderItemId = orderItemRepository.save(orderItem).getId();
            cartItemRepository.delete(cartItem);
            return orderItemId;

        } catch (Exception e) {
            throw new OrderException("Something went wrong");
        }

    }
}
