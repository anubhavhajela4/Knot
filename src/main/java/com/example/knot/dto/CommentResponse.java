package com.example.knot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CommentResponse {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private UUID postId;
    private UUID userId;
}
