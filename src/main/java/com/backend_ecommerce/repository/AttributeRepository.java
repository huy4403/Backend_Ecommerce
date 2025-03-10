package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    boolean existsByNameIgnoreCaseAndProductId(String name, Long productId);

    boolean existsByNameIgnoreCaseAndProductIdAndIdNot(String name, Long productId, Long id);

    List<Attribute> findByProductId(Long productId);
}
