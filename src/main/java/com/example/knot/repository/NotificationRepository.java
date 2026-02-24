package com.example.knot.repository;

import com.example.knot.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByRecipient_IdOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);
    long countByRecipient_IdAndReadFalse(UUID recipientId);
}
