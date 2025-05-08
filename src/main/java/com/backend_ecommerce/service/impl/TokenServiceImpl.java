package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.config.JwtProvider;
import com.backend_ecommerce.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProvider jwtProvider;

    @Override
    public void revokeToken(String token) {
        Date expiry = jwtProvider.getExpirationDateFromToken(token);
        long ttl = (expiry.getTime() - System.currentTimeMillis()) / 1000;
        if (ttl > 0) {
            redisTemplate.opsForValue().set("blacklist:" + token, "true", ttl, TimeUnit.SECONDS);
        }
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }
}
