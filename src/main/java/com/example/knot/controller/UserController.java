package com.example.knot.controller;

import com.example.knot.dto.UpdateUserRequest;
import com.example.knot.dto.UserResponse;
import com.example.knot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN') or @ownershipService.isOwner(#id,authentication)")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return "User Deleted Successfully";
    }


    // bad me check karlena ki koi aur na follow karva paye
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
