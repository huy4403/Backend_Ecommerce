package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.PaymentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaymentReturnResponse {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
