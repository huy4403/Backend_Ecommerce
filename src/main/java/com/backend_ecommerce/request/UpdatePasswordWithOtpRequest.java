package com.backend_ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePasswordWithOtpRequest {
    private String email;
    private String otp;
    private String newPassword;
    private String confirmPassword;
}
