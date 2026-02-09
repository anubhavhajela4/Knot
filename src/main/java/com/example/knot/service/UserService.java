package com.example.knot.service;

import com.example.knot.dto.RegisterUserRequest;
import com.example.knot.dto.UserResponse;
import com.example.knot.entity.User;
import com.example.knot.exception.EmailAlreadyExistsException;
import com.example.knot.exception.UserNotFoundException;
import com.example.knot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .bio(user.getBio())
                        .createdAt(user.getCreatedAt())
                        .build()
                )
                .toList();
    }

}
