package com.backend_ecommerce.repository;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByProductIdAndUserId(Long productId, Long userId);


    @Query("SELECT EXISTS (SELECT o FROM OrderItem o WHERE " +
            "o.product.id = :productId " +
            "AND o.userId = :userId " +
            "AND o.order.orderStatus = :orderStatus)")
    boolean existsByProductIdAndUserIdAndDelivered(@Param("productId") Long productId,
                                                   @Param("userId") Long userId,
                                                   @Param("orderStatus") OrderStatus orderStatus);
}
