package com.example.knot.controller;

import com.example.knot.dto.AuthResponse;
import com.example.knot.dto.LoginRequest;
import com.example.knot.dto.SignupRequest;
import com.example.knot.entity.User;
import com.example.knot.exception.EmailAlreadyExistsException;
import com.example.knot.exception.UserNotFoundException;
import com.example.knot.repository.UserRepository;
import com.example.knot.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {

        String email = request.getEmail();
        if(userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("User already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .bio(request.getBio())
                .role("USER")
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("Signup Successful")
                .build();
    }


    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());

        return AuthResponse.builder()
                .message("Login Successful")
                .token(token)
                .build();
    }


}
