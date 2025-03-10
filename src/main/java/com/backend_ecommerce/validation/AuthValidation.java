package com.backend_ecommerce.validation;

import com.backend_ecommerce.exception.ValidationException;
import com.backend_ecommerce.repository.UserRepository;
import com.backend_ecommerce.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthValidation {

    private final UserRepository userRepo;

    public void validateSignUpRequest(SignupRequest req) {
        List<String> errors = new ArrayList<>();
        if(!req.getPassword().equals(req.getRePassword()))
            errors.add("Passwords do not match");
        if(userRepo.existsByEmail(req.getEmail()))
            errors.add("Email address already in use");
        if(!errors.isEmpty())
            throw new ValidationException("Sign up failed", errors);
    }
}
