package com.backend_ecommerce.repository;

import com.backend_ecommerce.domain.TypeCode;
import com.backend_ecommerce.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    VerificationCode findByEmailAndType(String email, TypeCode typeCode);

    Optional<VerificationCode> findByEmailAndOtpAndType(String email, String otp, TypeCode type);
}
