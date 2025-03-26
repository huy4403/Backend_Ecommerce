package com.backend_ecommerce.request;

import com.backend_ecommerce.domain.PaymentStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdatePaymentStatusRequest {
    private PaymentStatus status;
}
