package com.backend_ecommerce.controller.cart;

import com.backend_ecommerce.request.CartItemRequest;
import com.backend_ecommerce.request.UpdateCartItemRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.CartResponse;
import com.backend_ecommerce.service.CartItemService;
import com.backend_ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<?> getUserCart() {
        CartResponse cartResponse = cartService.getUserCart();
        return ApiResponse.ok("Your cart:", cartResponse);
    }

    @PostMapping
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemRequest req) {
        Long cartItemId = cartItemService.addItemToCart(req);
        return ApiResponse.accepted("Product added to the cart", cartItemId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable("id") Long id,
                                            @Valid @RequestBody UpdateCartItemRequest req) {
        Long cartItemId = cartItemService.updateCartItem(id, req);
        return ApiResponse.accepted("Cart item updated", cartItemId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCartItem(@PathVariable("id") Long id) {
        cartItemService.removeCartItem(id);
        return ApiResponse.noContent("Cart item removed");
    }
}
