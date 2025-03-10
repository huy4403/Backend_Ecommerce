package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Attribute;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class GetAttributeResponse {
    private Long id;
    private String name;

    public static GetAttributeResponse mapFromAttribute(Attribute attribute){

        return GetAttributeResponse
                .builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .build();

    }
}
