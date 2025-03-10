package com.backend_ecommerce.repository;

import com.backend_ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long id);

    Optional<Address> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long id1);
}
