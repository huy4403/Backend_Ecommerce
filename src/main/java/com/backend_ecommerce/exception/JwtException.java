package com.backend_ecommerce.exception;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
