package com.backend_ecommerce.model;

import com.backend_ecommerce.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Product product;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_variant_attribute_values",
            joinColumns = @JoinColumn(name = "product_variant_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id"))
    @Builder.Default
    private List<AttributeValue> attributeValues = new ArrayList<>();

    private int quantity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;
}
