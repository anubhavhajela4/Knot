package com.example.knot.config;

import com.example.knot.entity.User;
import com.example.knot.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            String adminEmail = "admin@knot.com";

            if(userRepository.findByEmail(adminEmail).isEmpty()) {

                User admin = User.builder()
                        .name("admin")
                        .email(adminEmail)
                        .password(passwordEncoder.encode("admin"))
                        .role("ADMIN")
                        .build();
                userRepository.save(admin);

                System.out.println("Admin created");

            }

        };
    }

}
