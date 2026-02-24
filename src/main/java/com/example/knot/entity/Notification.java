package com.example.knot.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User recipient;

    @ManyToOne(optional = false)
    private User actor;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private UUID postId;

    private UUID commentId;

    private boolean read = false;

    private LocalDateTime createdAt;


}
