package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndProductVariantId(Long id, Long productVariantId);

    Optional<CartItem> findByIdAndCartId (Long id, Long currentUserCartId);

    boolean existsByIdAndCartId(Long id, Long currentUserCartId);

    Long countCartItemsByUserId(Long id);

    @Query("""
        SELECT DISTINCT u.email
        FROM CartItem c
        JOIN User u ON u.id = c.userId
        WHERE c.productVariant.id = :variantId AND c.quantity > :oldQuantity
    """)
    List<String> findAllUserEmailWantBuy(@Param("variantId") Long variantId,
                                         @Param("oldQuantity") int oldQuantity);
}
