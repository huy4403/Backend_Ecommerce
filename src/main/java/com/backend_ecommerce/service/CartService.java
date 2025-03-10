package com.backend_ecommerce.service;

import com.backend_ecommerce.model.User;
import com.backend_ecommerce.response.CartResponse;

public interface CartService {

    Long createCart(User user);

    CartResponse getUserCart();

}
