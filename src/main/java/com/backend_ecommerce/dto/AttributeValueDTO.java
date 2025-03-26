package com.backend_ecommerce.dto;

import com.backend_ecommerce.model.AttributeValue;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttributeValueDTO {
    private Long id;
    private String name;
    private String value;

    public static AttributeValueDTO mapFrom(AttributeValue attributeValue) {
        return AttributeValueDTO
                .builder()
                .id(attributeValue.getAttribute().getId())
                .name(attributeValue.getAttribute().getName())
                .value(attributeValue.getValue())
                .build();
    }
}
