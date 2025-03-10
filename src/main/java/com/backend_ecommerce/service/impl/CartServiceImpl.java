package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.CartException;
import com.backend_ecommerce.model.Cart;
import com.backend_ecommerce.model.User;
import com.backend_ecommerce.repository.CartRepository;
import com.backend_ecommerce.response.CartResponse;
import com.backend_ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Long createCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        try {
            return cartRepository.save(newCart).getId();
        } catch (Exception e) {
            throw new CartException("Something went wrong...");
        }
    }

    @Override
    public CartResponse getUserCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Cart cart = cartRepository.findByUserId(userPrincipal.user().getId());
        return CartResponse.mapFromCart(cart);
    }
}
