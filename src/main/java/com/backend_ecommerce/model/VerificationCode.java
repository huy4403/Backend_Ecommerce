package com.backend_ecommerce.model;

import com.backend_ecommerce.domain.TypeCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String otp;

    @Enumerated(EnumType.STRING)
    private TypeCode type;

    @Builder.Default
    private LocalDateTime expiryDate = LocalDateTime.now();
}
