package com.example.knot.controller;

import com.example.knot.dto.NotificationResponse;
import com.example.knot.entity.User;
import com.example.knot.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Page<NotificationResponse> getMyNotifications(
            Authentication authentication,
            Pageable pageable
    ) {
        UUID currentUserId = (UUID) authentication.getPrincipal();
        return notificationService.getUserNotifications(
                currentUserId,
                pageable
        );
    }

    @GetMapping("/unread-count")
    public long getUnreadCount(Authentication authentication) {
        UUID currentUserId = (UUID) authentication.getPrincipal();
        return notificationService.getUnreadCount(currentUserId);
    }

    @PutMapping("/{id}/read")
    public void markAsRead(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        UUID currentUserId = (UUID) authentication.getPrincipal();
        notificationService.markAsRead(id, currentUserId);
    }

    @PutMapping("/mark-all-read")
    public void markAllAsRead(Authentication authentication) {
        UUID currentUserId = (UUID) authentication.getPrincipal();
        notificationService.markAllAsRead(currentUserId);
    }


}