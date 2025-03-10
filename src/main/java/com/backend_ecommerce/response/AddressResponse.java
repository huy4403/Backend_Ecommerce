package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Address;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddressResponse {

    private Long id;

    private String fullName;

    private String mobile;

    private String address;

    private String description;

    public static AddressResponse mapFromAddress(Address address) {
        return AddressResponse
                .builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .mobile(address.getMobile())
                .address(address.getAddress())
                .description(address.getDescription())
                .build();
    }
}
