package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.TypeCode;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.exception.VerificationCodeException;
import com.backend_ecommerce.model.VerificationCode;
import com.backend_ecommerce.repository.VerificationCodeRepository;
import com.backend_ecommerce.service.VerificationCodeService;
import com.backend_ecommerce.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode generateCode(String email, TypeCode type) {

        VerificationCode verificationCode = verificationCodeRepository.findByEmailAndType(email, type);

        if(verificationCode != null) verificationCodeRepository.delete(verificationCode);
        String otp = OtpUtils.generateOTP();
        VerificationCode newVerificationCode
                = VerificationCode
                .builder()
                .email(email)
                .otp(otp)
                .type(type)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        return verificationCodeRepository.save(newVerificationCode);
    }

    @Override
    public void findVerificationCode(String email, String otp, TypeCode type) {

        VerificationCode verificationCode
                = verificationCodeRepository.findByEmailAndOtpAndType(email, otp, type).orElseThrow(
                () -> new ResourceNotFoundException("OTP is incorrect or does not exist")
        );

        if(verificationCode.getExpiryDate().isBefore(LocalDateTime.now()))
                throw new VerificationCodeException("Expired verification code");

        verificationCodeRepository.delete(verificationCode);
    }
}
