package com.example.knot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class KnotApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnotApplication.class, args);
    }

}
