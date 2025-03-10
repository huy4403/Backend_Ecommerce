package com.backend_ecommerce.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreatePaymentRequest {
    private Long orderId;
    private Long amount;
}
