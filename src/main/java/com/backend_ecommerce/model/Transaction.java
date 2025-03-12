package com.backend_ecommerce.model;

import com.backend_ecommerce.domain.PaymentMethod;
import com.backend_ecommerce.domain.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;
}
