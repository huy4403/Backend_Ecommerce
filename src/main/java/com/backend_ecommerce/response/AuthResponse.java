package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.ROLE_NAME;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String fullName;

    private String avatar;

    private String token;

    private ROLE_NAME role;
}