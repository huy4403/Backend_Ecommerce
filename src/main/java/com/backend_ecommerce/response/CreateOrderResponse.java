package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.PaymentMethod;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateOrderResponse {

    private Long orderId;
    private Long userId;
    private Long totalPrice;
    private PaymentMethod paymentMethod;
    private String paymentUrl;
}
