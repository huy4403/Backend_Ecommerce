package com.backend_ecommerce.repository;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
            "AND (:minPrice IS NULL OR :minPrice = 0 OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR :maxPrice = 0 OR p.price <= :maxPrice) " +
            "AND p.status = :productStatus " +
            "AND LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Product> filterProduct(@Param("title") String title,
                                @Param("categoryId") Long categoryId,
                                @Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                @Param("productStatus") ProductStatus productStatus,
                                Pageable pageable);
}
