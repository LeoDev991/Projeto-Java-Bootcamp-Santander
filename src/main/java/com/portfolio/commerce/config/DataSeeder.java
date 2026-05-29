package com.portfolio.commerce.config;

import com.portfolio.commerce.entity.Role;
import com.portfolio.commerce.entity.User;
import com.portfolio.commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    @Bean
    CommandLineRunner seedUsers(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!repository.existsByEmail("admin@commerce.dev")) {
                repository.save(new User(
                        "admin@commerce.dev",
                        passwordEncoder.encode("Admin@123"),
                        Set.of(Role.ADMIN, Role.CUSTOMER)
                ));
            }
            if (!repository.existsByEmail("customer@commerce.dev")) {
                repository.save(new User(
                        "customer@commerce.dev",
                        passwordEncoder.encode("Customer@123"),
                        Set.of(Role.CUSTOMER)
                ));
            }
        };
    }
}
