package com.backend_ecommerce.response;

import com.backend_ecommerce.model.AttributeValue;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttributeValueResponse {
    private Long id;
    private String value;

    public static AttributeValueResponse mapFromAttributeValue(AttributeValue attributeValue) {
        return AttributeValueResponse
                .builder()
                .id(attributeValue.getId())
                .value(attributeValue.getValue())
                .build();
    }
}
