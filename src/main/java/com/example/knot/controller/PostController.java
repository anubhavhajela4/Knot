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

    @PostMapping("/{postId}/like/{userId}")
    public String likePost(@PathVariable UUID postId, @PathVariable UUID userId) {
        postService.likePost(postId, userId);
        return "Post Liked";
    }

    @DeleteMapping("/{postId}/like/{userId}")
    public String unlikePost(@PathVariable UUID postId, @PathVariable UUID userId) {
        postService.unlikePost(postId, userId);
        return "Post Unliked";
    }

    @GetMapping("/{postId}/likes")
    public int getLikes(@PathVariable UUID postId) {
        return postService.getLikeCount(postId);
    }
}
