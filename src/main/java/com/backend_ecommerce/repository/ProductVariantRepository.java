package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.AttributeValue;
import com.backend_ecommerce.model.ProductVariant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @Query("SELECT pv FROM ProductVariant pv " +
            "JOIN pv.attributeValues av " +
            "WHERE pv.product.id = :productId " +
            "And av IN :attributeValues " +
            "GROUP BY pv.id ")
    Optional<ProductVariant> findByProductIdAndAttributeValues(Long productId, List<AttributeValue> attributeValues);

    @Query("SELECT pv FROM ProductVariant pv " +
            "JOIN pv.attributeValues av " +
            "WHERE pv.product.id = :productId " +
            "AND av.value IN :attributeValueIds " +
            "GROUP BY pv.id " +
            "HAVING COUNT(av.id) = :size")
    Optional<ProductVariant> findByProductIdAndAttributeValueIds(
            @Param("productId") Long productId,
            @Param("attributeValueIds") List<Long> attributeValueIds,
            @Param("size") Integer size);

    @Query("SELECT EXISTS (SELECT pv FROM ProductVariant pv " +
            "LEFT JOIN pv.attributeValues av " +
            "WHERE pv.product.id = :productId " +
            "AND (av.value IN :attributeValues OR (:size = 0 AND av.id IS NULL)) " +
            "GROUP BY pv.id " +
            "HAVING COUNT(av.id) = :size)")
    boolean existsByProductAndAttributeValues(
            @Param("productId") Long productId,
            @Param("attributeValues") List<String> attributeValues,
            @Param("size") Integer size);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant p SET p.quantity = p.quantity - :quantity WHERE p.id = :id")
    void decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant p SET p.quantity = p.quantity + :quantity WHERE p.id = :id")
    void increaseStock(@Param("id") Long id, @Param("quantity") int quantity);
}
