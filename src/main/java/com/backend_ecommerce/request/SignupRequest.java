package com.backend_ecommerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Full name not blank")
    private String fullName;
    @NotBlank(message = "Email not blank")
    private String email;
    @NotBlank(message = "Password not blank")
    private String password;
    @NotBlank(message = "Repassword not blank")
    private String rePassword;
}
