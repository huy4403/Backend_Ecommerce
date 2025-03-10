package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.AddressException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Address;
import com.backend_ecommerce.repository.AddressRepository;
import com.backend_ecommerce.request.AddressRequest;
import com.backend_ecommerce.response.AddressResponse;
import com.backend_ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Long createAddress(AddressRequest req) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Address address = new Address();
        address.setFullName(req.getFullName());
        address.setMobile(req.getMobile());
        address.setAddress(req.getAddress());
        address.setDescription(req.getDescription());
        address.setUser(userPrincipal.user());

        try {
            return addressRepository.save(address).getId();
        }
        catch (Exception e) {
            throw new AddressException("Something went wrong...");
        }

    }

    @Override
    public Long updateAddressById(Long id, AddressRequest req) {
        Address existAddress = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id: " + id)
        );
        existAddress.setFullName(req.getFullName());
        existAddress.setMobile(req.getMobile());
        existAddress.setAddress(req.getAddress());
        existAddress.setDescription(req.getDescription());

        try {
            return addressRepository.save(existAddress).getId();
        } catch (Exception e) {
            throw new AddressException("Something went wrong...");
        }
    }

    @Override
    public List<AddressResponse> getAllAddressByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        List<Address> addresses = addressRepository.findByUserId(userPrincipal.user().getId());
        if(addresses.isEmpty())
            throw new ResourceNotFoundException("Address not found with user id: " + userPrincipal.user().getId());

        return addresses.stream()
                .map(AddressResponse::mapFromAddress)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddressById(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Address address = addressRepository.findByIdAndUserId(id, userPrincipal.user().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id: " + id)
        );

        return AddressResponse.mapFromAddress(address);
    }

    @Override
    public void deleteAddressById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        if(!addressRepository.existsByIdAndUserId(id, userPrincipal.user().getId()))
            throw new ResourceNotFoundException("Address not found in your address list");
        addressRepository.deleteById(id);
    }
}
