package com.example.knot.service;

import com.example.knot.dto.UpdateUserRequest;
import com.example.knot.dto.UserResponse;
import com.example.knot.entity.User;
import com.example.knot.exception.AlreadyFollowingException;
import com.example.knot.exception.NotFollowingException;
import com.example.knot.exception.UnauthorizedActionException;
import com.example.knot.exception.UserNotFoundException;
import com.example.knot.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserResponse getCurrentUser() {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

        UUID userId = getCurrentUserId();

        if(!userId.equals(id)) {
            throw new UnauthorizedActionException("Cant update others profiles");
        }

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

        UUID userId = getCurrentUserId();
        if(!userId.equals(id)) {
            throw new UnauthorizedActionException("Cant delete others profiles");
        }

        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        userRepository.delete(user);
    }

    public void followUser(UUID userId, UUID targetId) {
        if(userId.equals(targetId)) {
            throw new AlreadyFollowingException("You can't follow yourself");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        User targetUser = userRepository.findById(targetId)
                .orElseThrow(()->new UserNotFoundException("Target User Not Found"));
        if(user.getFollowing().contains(targetUser)) {
            throw new AlreadyFollowingException("You can't follow the same user twice");
        }
        user.getFollowing().add(targetUser);
        userRepository.save(user);
    }

    public void unfollowUser(UUID userId, UUID targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        User targetUser = userRepository.findById(targetId)
                .orElseThrow(()->new UserNotFoundException("Target User Not Found"));
        if(!user.getFollowing().contains(targetUser)) {
            throw new NotFollowingException("You are not following this user");
        }
        user.getFollowing().remove(targetUser);
        userRepository.save(user);
    }

    public List<UserResponse> getFollowers(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        return user.getFollowers()
                .stream()
                .map(user1 -> UserResponse.builder()
                        .id(user1.getId())
                        .name(user1.getName())
                        .email(user1.getEmail())
                        .bio(user1.getBio())
                        .createdAt(user1.getCreatedAt())
                        .build())
                .toList();
    }

    public List<UserResponse> getFollowing(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        return user.getFollowing()
                .stream()
                .map(user1-> UserResponse.builder()
                        .id(user1.getId())
                        .name(user1.getName())
                        .email(user1.getEmail())
                        .bio(user1.getBio())
                        .createdAt(user1.getCreatedAt())
                        .build())
                .toList();
    }

}
