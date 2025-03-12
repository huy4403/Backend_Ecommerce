package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByOrderId(Long orderId);
}
