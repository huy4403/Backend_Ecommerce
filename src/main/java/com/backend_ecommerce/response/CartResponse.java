package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Cart;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CartResponse {

    private Long id;

    private Set<CartItemResponse> cartItems = new HashSet<>();

    public static CartResponse mapFromCart(Cart cart) {
        CartResponse cartResponse = new CartResponse();

        cartResponse.setId(cart.getId());

        cartResponse.setCartItems(
                cart.getCartItems().stream().map(CartItemResponse::mapFromCart)
                        .collect(Collectors.toSet())
        );

        return cartResponse;
    }

}
