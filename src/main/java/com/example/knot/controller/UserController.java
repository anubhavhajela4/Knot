package com.example.knot.controller;

import com.example.knot.dto.RegisterUserRequest;
import com.example.knot.dto.UpdateUserRequest;
import com.example.knot.dto.UserResponse;
import com.example.knot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody RegisterUserRequest  request) {
        return userService.registerUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return "User Deleted Successfully";
    }

    @PostMapping("{userId}/follow/{targetId}")
    public String followUser(@PathVariable UUID userId,@PathVariable UUID targetId) {
        userService.followUser(userId, targetId);
        return "User Followed Successfully";
    }

    @DeleteMapping("{userId}/follow/{targetId}")
    public String unfollowUser(@PathVariable UUID userId,@PathVariable UUID targetId) {
        userService.unfollowUser(userId, targetId);
        return "User Unfollowed Successfully";
    }

    @GetMapping("/{userId}/following")
    public List<UserResponse> getFollowing(@PathVariable UUID userId) {
        return userService.getFollowing(userId);
    }

    @GetMapping("/{userId}/followers")
    public List<UserResponse> getFollowers(@PathVariable UUID userId) {
        return userService.getFollowers(userId);
    }

}
