package com.backend_ecommerce.repository;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryName IS NULL OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND p.status = :productStatus " +
            "AND (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Product> filterProduct(@Param("title") String title,
                                @Param("categoryName") String categoryName,
                                @Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                @Param("productStatus") ProductStatus productStatus,
                                Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:category IS NULL OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Product> findProductManagement(@Param("title") String title,
                                @Param("category") String category,
                                @Param("minPrice") Integer minPrice,
                                @Param("maxPrice") Integer maxPrice,
                                Pageable pageable);

    List<Product> findTop4ByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p.id ORDER BY COALESCE(AVG(r.rating), 0) DESC")
    Page<Product> findTopRatedProducts(Pageable pageable);
}
