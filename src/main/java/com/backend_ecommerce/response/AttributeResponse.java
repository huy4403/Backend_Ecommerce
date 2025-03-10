package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttributeResponse {
    private String key;
    private List<AttributeValueResponse> values;

    public static AttributeResponse mapFromAttribute(Attribute attribute) {

        AttributeResponse attributeResponse = new AttributeResponse();
        attributeResponse.setKey(attribute.getName());

        List<AttributeValueResponse> values = attribute.getAttributeValues().stream().map(
                AttributeValueResponse::mapFromAttributeValue
        ).collect(Collectors.toList());

        attributeResponse.setValues(values);

        return attributeResponse;
    }
}
