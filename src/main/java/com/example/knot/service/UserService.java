package com.example.knot.service;

import com.example.knot.dto.UpdateUserRequest;
import com.example.knot.dto.UserResponse;
import com.example.knot.entity.NotificationType;
import com.example.knot.entity.User;
import com.example.knot.exception.AlreadyFollowingException;
import com.example.knot.exception.NotFollowingException;
import com.example.knot.exception.UnauthorizedActionException;
import com.example.knot.exception.UserNotFoundException;
import com.example.knot.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    public UserService (UserRepository userRepository,
                        ModelMapper modelMapper,
                        NotificationService notificationService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
    }


    public UserResponse getCurrentUser() {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return modelMapper.map(user, UserResponse.class);
    }

    public UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        return modelMapper.map(user, UserResponse.class);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
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
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
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
        notificationService.createNotification(
                user,
                targetUser,
                NotificationType.FOLLOW,
                null,
                null
        );
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
                .map(user1 -> modelMapper.map(user1, UserResponse.class))
                .toList();
    }

    public List<UserResponse> getFollowing(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        return user.getFollowing()
                .stream()
                .map(user1 -> modelMapper.map(user1, UserResponse.class))
                .toList();
    }

}
