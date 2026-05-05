package com.example.knot.dto;

import com.example.knot.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private UUID id;
    private UUID actorId;
    private String actorUsername;
    private NotificationType type;
    private UUID postId;
    private UUID commentId;
    private boolean read;
    private LocalDateTime timestamp;
}
