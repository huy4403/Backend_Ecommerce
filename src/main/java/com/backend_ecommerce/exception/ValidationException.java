package com.backend_ecommerce.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationException extends RuntimeException{
    private List<String> errors;
    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
}
