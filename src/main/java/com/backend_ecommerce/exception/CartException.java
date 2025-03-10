package com.backend_ecommerce.exception;

public class CartException extends RuntimeException {
    public CartException(String message) {
        super(message);
    }
}
