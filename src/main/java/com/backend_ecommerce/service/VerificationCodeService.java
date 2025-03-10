package com.backend_ecommerce.service;

import com.backend_ecommerce.domain.TypeCode;
import com.backend_ecommerce.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode generateCode(String email, TypeCode type);

    void findVerificationCode(String email, String otp, TypeCode type);
}
