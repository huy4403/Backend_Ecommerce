package com.backend_ecommerce.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttributeValueRequest {
    private Long attributeId;
    private String value;
}
