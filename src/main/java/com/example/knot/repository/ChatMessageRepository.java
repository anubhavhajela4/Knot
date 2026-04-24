package com.example.knot.repository;

import com.example.knot.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    /**
     * Fetches the full conversation between two users, ordered chronologically.
     */
    @Query("SELECT m FROM ChatMessage m " +
           "WHERE (m.sender.id = :user1 AND m.recipient.id = :user2) " +
           "   OR (m.sender.id = :user2 AND m.recipient.id = :user1) " +
           "ORDER BY m.timestamp ASC")
    List<ChatMessage> findConversation(@Param("user1") UUID user1,
                                       @Param("user2") UUID user2);

    /**
     * Fetches all messages received by a user, most recent first.
     */
    List<ChatMessage> findByRecipientIdOrderByTimestampDesc(UUID recipientId);
}
