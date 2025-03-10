package com.backend_ecommerce.service;

import com.backend_ecommerce.model.CartItem;
import com.backend_ecommerce.model.Order;

public interface OrderItemService {

    Long addOrderItem(CartItem cartItem, Order order);

}
