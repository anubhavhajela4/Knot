package com.example.knot.service;

import com.example.knot.entity.Notification;
import com.example.knot.entity.NotificationType;
import com.example.knot.entity.User;
import com.example.knot.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(
            User recipient,
            User actor,
            NotificationType type,
            UUID postId,
            UUID commentId
    ) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .actor(actor)
                .type(type)
                .postId(postId)
                .commentId(commentId)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    public Page<Notification> getUserNotifications(UUID userId, Pageable pageable) {
        return notificationRepository.findByRecipient_IdOrderByCreatedAtDesc(userId, pageable);
    }

    public long getUserNotificationsCount(UUID userId) {
        return notificationRepository.countByRecipient_IdAndReadFalse(userId);
    }

    @Transactional
    public void markAsRead(UUID notificationId, UUID userId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getRecipient().getId().equals(userId)) {
            throw new RuntimeException("Not authorized");
        }

        notification.setRead(true);
    }
}
