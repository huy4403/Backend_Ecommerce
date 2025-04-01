package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.Order;
import com.backend_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> getByOrderDate(LocalDateTime startDate, LocalDateTime endDate);

    Optional<Order> findByIdAndUser(Long id, User user);

    List<Order> findAllByUser(User user);
}
