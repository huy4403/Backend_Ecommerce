package com.backend_ecommerce.domain;

public enum AccountStatus {
    ACTIVE,               // Account is active and in good standing
    BANNED,               // Account is permanently banned due to severe violations
    CLOSED                // Account is permanently closed, possibly at user request
}

