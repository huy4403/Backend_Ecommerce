package com.backend_ecommerce.repository;

import com.backend_ecommerce.domain.AccountStatus;
import com.backend_ecommerce.domain.ROLE_NAME;
import com.backend_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String username);

    boolean existsByMobileAndIdNot(String mobile, Long id);

    boolean existsByEmailAndAccountStatus(String email, AccountStatus accountStatus);

    List<User> findAllByRoleNot(ROLE_NAME roleName);

    Long countUsersByRoleNot(ROLE_NAME roleName);

    boolean existsByMobile(String mobile);

    Optional<User> findByRole(ROLE_NAME roleName);
}
