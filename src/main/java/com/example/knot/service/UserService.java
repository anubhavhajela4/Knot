package com.example.knot.service;

import com.example.knot.dto.RegisterUserRequest;
import com.example.knot.dto.UserResponse;
import com.example.knot.entity.User;
import com.example.knot.exception.EmailAlreadyExistsException;
import com.example.knot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(RegisterUserRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .bio(request.getBio())
                .build();


        User savedUser = userRepository.save(user);
        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .bio(savedUser.getBio())
                .createdAt(savedUser.getCreatedAt())
                .build();

    }
}
