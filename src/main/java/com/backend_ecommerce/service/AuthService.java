package com.backend_ecommerce.service;

import com.backend_ecommerce.request.*;
import com.backend_ecommerce.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {

    AuthResponse signupHandler(SignupRequest req);

    AuthResponse signingHandler(SigningWithOtpRequest req);

    AuthResponse signingHandler(SigningWithPasswordRequest req);

    String generateSigningOtpCode(SentOtpRequest req) throws MessagingException;

    String generateResetPasswordOtpCode(SentOtpRequest req) throws MessagingException;

    String updatePasswordWithOtp(UpdatePasswordWithOtpRequest req);
}
