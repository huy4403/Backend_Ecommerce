package com.backend_ecommerce.service;

import com.backend_ecommerce.request.AddressRequest;
import com.backend_ecommerce.response.AddressResponse;

import java.util.List;

public interface AddressService {
    Long createAddress(AddressRequest req);

    Long updateAddressById(Long id, AddressRequest req);

    List<AddressResponse> getAllAddressByUser();

    AddressResponse getAddressById(Long id);

    void deleteAddressById(Long id);
}
