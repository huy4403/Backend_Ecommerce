package com.backend_ecommerce.request;

import com.backend_ecommerce.domain.TransactionStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateTransactionStatusRequest {
    private TransactionStatus status;
}
