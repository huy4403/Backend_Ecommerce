package com.backend_ecommerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    @NotBlank(message = "Full name not blank")
    private String fullName;

    @Builder.Default
    private String mobile = null;

    @NotBlank(message = "Email not blank")
    private String email;
    @NotBlank(message = "Password not blank")
    private String password;
    @NotBlank(message = "Repassword not blank")
    private String rePassword;
}
