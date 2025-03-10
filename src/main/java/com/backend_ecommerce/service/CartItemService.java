package com.backend_ecommerce.service;

import com.backend_ecommerce.request.CartItemRequest;
import com.backend_ecommerce.request.UpdateCartItemRequest;

public interface CartItemService {
    Long addItemToCart(CartItemRequest req);

    Long updateCartItem(Long id, UpdateCartItemRequest req);

    void removeCartItem(Long id);
}
