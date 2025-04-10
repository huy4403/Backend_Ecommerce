package com.backend_ecommerce.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RePaymentResponse {
    private Long orderId;
    private Long totalPrice;
    private String paymentUrl;
}
