package com.example.knot.controller;

import com.example.knot.dto.CommentResponse;
import com.example.knot.dto.CreateCommentRequest;
import com.example.knot.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/{postId}/user/{userId}")
    public CommentResponse createComment(
            @PathVariable UUID postId,
            @PathVariable UUID userId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        return commentService.createComment(userId, postId, request);
    }

    @GetMapping("/{postId}")
    public List<CommentResponse> getComments(@PathVariable UUID postId) {
        return commentService.getComments(postId);
    }


}

