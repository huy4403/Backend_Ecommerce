package com.backend_ecommerce.request;

import com.backend_ecommerce.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateOrderStatusRequest {
    OrderStatus orderStatus;
}
