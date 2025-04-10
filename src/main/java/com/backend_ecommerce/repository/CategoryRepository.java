package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByNameIgnoreCase(String name);

    List<Category> findByParentId(Long id);

    @Query("SELECT c FROM Category c WHERE :id IS NULL OR c.id <> :id")
    List<Category> findAllAndIdNot(@Param("id") Long id);
}
