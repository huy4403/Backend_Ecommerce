package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {

    Optional<AttributeValue> findByAttributeIdAndValue(Long attributeId, String value);
}
