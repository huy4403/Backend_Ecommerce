package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndProductVariantId(Long id, Long productVariantId);

    Optional<CartItem> findByIdAndCartId (Long id, Long currentUserCartId);

    boolean existsByIdAndCartId(Long id, Long currentUserCartId);
}
