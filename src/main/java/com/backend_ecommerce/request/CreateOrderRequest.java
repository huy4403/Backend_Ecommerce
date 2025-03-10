package com.backend_ecommerce.request;

import com.backend_ecommerce.domain.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {
    private Long addressId;
    private List<Long> cartItemIds;
    private PaymentMethod paymentMethod;
}
