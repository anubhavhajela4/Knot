package com.example.knot.service;

import com.example.knot.dto.RegisterUserRequest;
import com.example.knot.dto.UpdateUserRequest;
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
                        .email(user.getEmail())
                        .bio(user.getBio())
                        .createdAt(user.getCreatedAt())
                        .build()
                )
                .toList();
    }

    public UserResponse updateUser(UUID id, UpdateUserRequest request) {

        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        user.setBio(request.getBio());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        User updatedUser = userRepository.save(user);
        return UserResponse.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .bio(updatedUser.getBio())
                .createdAt(updatedUser.getCreatedAt())
                .build();
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        userRepository.delete(user);
    }

    public void followUser(UUID userId, UUID targetId) {
        if(userId.equals(targetId)) {}
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        User targetUser = userRepository.findById(targetId)
                .orElseThrow(()->new UserNotFoundException("Target User Not Found"));
        user.getFollowing().add(targetUser);
        userRepository.save(user);
    }

}
