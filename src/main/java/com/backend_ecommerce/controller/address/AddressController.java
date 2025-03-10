package com.backend_ecommerce.controller.address;

import com.backend_ecommerce.request.AddressRequest;
import com.backend_ecommerce.response.AddressResponse;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<?> addAddess(@RequestBody AddressRequest req) {
        Long addressId = addressService.createAddress(req);
        return ApiResponse.created("Address added", addressId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Long id,
                                           @RequestBody AddressRequest req) {
        Long addressId = addressService.updateAddressById(id, req);
        return ApiResponse.accepted("Address updated", addressId);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllAddresses() {
        List<AddressResponse> addressResponse = addressService.getAllAddressByUser();
        return ApiResponse.ok("Your address list", addressResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable("id") Long id) {
        AddressResponse addressResponse = addressService.getAddressById(id);
        return ApiResponse.ok("Your address", addressResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) {
        addressService.deleteAddressById(id);
        return ApiResponse.accepted("Address deleted", id);
    }

}
