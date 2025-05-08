package com.backend_ecommerce.service;

public interface TokenService {
    void revokeToken(String token);
    boolean isTokenRevoked(String token);
}
