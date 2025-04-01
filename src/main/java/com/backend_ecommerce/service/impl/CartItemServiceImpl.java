package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.CartException;
import com.backend_ecommerce.exception.ProductException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Cart;
import com.backend_ecommerce.model.CartItem;
import com.backend_ecommerce.model.ProductVariant;
import com.backend_ecommerce.repository.CartItemRepository;
import com.backend_ecommerce.repository.ProductVariantRepository;
import com.backend_ecommerce.request.CartItemRequest;
import com.backend_ecommerce.request.UpdateCartItemRequest;
import com.backend_ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final ProductVariantRepository productVariantRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Long addItemToCart(CartItemRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        ProductVariant productVariant = productVariantRepository.findById(req.getProductVariantId()).orElseThrow(
                () -> new ResourceNotFoundException("Product variant not found")
        );

        if(productVariant.getProduct().getStatus() != ProductStatus.ACTIVE) {
            throw new ProductException("Product not active");
        }

        Cart currentUserCart = userPrincipal.user().getCart();

        CartItem existCartItem = cartItemRepository.findByCartIdAndProductVariantId(
                currentUserCart.getId(), req.getProductVariantId());

        if(existCartItem != null) {
            throw new CartException("Sản phẩm đã có trong giỏ hàng");
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(currentUserCart);
        cartItem.setProductVariant(productVariant);
        cartItem.setQuantity(req.getQuantity());
        cartItem.setUserId(userPrincipal.user().getId());
        return cartItemRepository.save(cartItem).getId();

    }

    @Override
    public Long updateCartItem(Long id, UpdateCartItemRequest req) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Long currentUserCartId = userPrincipal.user().getCart().getId();

        CartItem existCartItem = cartItemRepository.findByIdAndCartId(id, currentUserCartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart item not found in your cart")
        );

        existCartItem.setQuantity(existCartItem.getQuantity() + req.getQuantity());

        return cartItemRepository.save(existCartItem).getId();
    }

    @Override
    public void removeCartItem(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Long currentUserCartId = userPrincipal.user().getCart().getId();

        if(!cartItemRepository.existsByIdAndCartId(id, currentUserCartId))
            throw new ResourceNotFoundException("Cart item not found in your cart");

        cartItemRepository.deleteById(id);

    }

    @Override
    public Long getCartCount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        return cartItemRepository.countCartItemsByUserId(userPrincipal.user().getId());
    }
}
