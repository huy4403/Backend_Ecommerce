package com.backend_ecommerce;

import com.backend_ecommerce.domain.AccountStatus;
import com.backend_ecommerce.domain.ROLE_NAME;
import com.backend_ecommerce.model.*;
import com.backend_ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeUser();
    }

    private void initializeUser() {
        List<User> users = new ArrayList<>();
        User admin = new User();
        admin.setFullName("Doan Huy");
        admin.setEmail("huy4403nd@gmail.com");
        admin.setPassword(passwordEncoder.encode("4403"));
        admin.setRole(ROLE_NAME.ADMIN);
        admin.setAccountStatus(AccountStatus.ACTIVE);
        users.add(admin);

        userRepository.saveAll(users);
    }
}
