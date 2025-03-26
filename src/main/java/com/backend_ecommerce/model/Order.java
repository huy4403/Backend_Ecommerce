package com.backend_ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Address shippingAddress;

    private Long totalPrice;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    private int totalItem;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus=PaymentStatus.PENDING;

    @OneToOne(mappedBy = "order", optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private Transaction transaction;

    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime deliverDate = LocalDateTime.now().plusDays(7);

    @UpdateTimestamp
    @Column(name = "updatedAt")
    @JsonIgnore
    private LocalDateTime updatedAt;

}
