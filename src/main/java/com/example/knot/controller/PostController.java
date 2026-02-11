package com.example.knot.controller;

import com.example.knot.dto.CreatePostRequest;
import com.example.knot.dto.PostResponse;
import com.example.knot.service.PostService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/users/{userId}")
    public PostResponse createPost(@PathVariable UUID userId,
                                   @Valid @RequestBody CreatePostRequest request) {
        return postService.createPost(userId, request);
    }

    @GetMapping("/users/{userId}")
    public List<PostResponse> getpostByUser(@PathVariable UUID userId) {
        return postService.getPostByUser(userId);
    }
}
