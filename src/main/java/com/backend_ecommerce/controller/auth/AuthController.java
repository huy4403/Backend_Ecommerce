package com.backend_ecommerce.controller.auth;

import com.backend_ecommerce.request.*;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.AuthResponse;
import com.backend_ecommerce.service.AuthService;
import com.backend_ecommerce.service.TokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        AuthResponse authResponse = authService.signupHandler(req);
        return ApiResponse.created("SignUp successfully", authResponse);
    }

    @PostMapping("/signing")
    public ResponseEntity<?> signIng(@RequestBody SigningWithPasswordRequest req) {
        AuthResponse authResponse = authService.signingHandler(req);
        return ApiResponse.ok("Signing Successful", authResponse);
    }

    @PostMapping("/signing-with-otp")
    public ResponseEntity<?> signIngWithOtp(@RequestBody SigningWithOtpRequest req) {
        AuthResponse authResponse = authService.signingHandler(req);
        return ApiResponse.ok("Signing Successful", authResponse);
    }

    @PostMapping("/sent-signing-otp")
    public ResponseEntity<?> sentOtp(@RequestBody SentOtpRequest req) throws MessagingException {
        String message = authService.generateSigningOtpCode(req);
        return ApiResponse.created(message); //OTP sent successfully
    }

    @PostMapping("/sent-reset-password-otp")
    public ResponseEntity<?> sentOtpResetPassword(@RequestBody SentOtpRequest req) throws MessagingException {
        String message = authService.generateResetPasswordOtpCode(req);
        return ApiResponse.created(message);
    }

    @PutMapping("/reset-password-with-otp")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordWithOtpRequest req) {
        String message = authService.updatePasswordWithOtp(req);
        return ApiResponse.accepted(message);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenService.revokeToken(token);
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}
