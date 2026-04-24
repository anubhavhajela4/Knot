package com.example.knot.service;

import com.example.knot.dto.ChatMessageRequest;
import com.example.knot.dto.ChatMessageResponse;
import com.example.knot.entity.ChatMessage;
import com.example.knot.entity.User;
import com.example.knot.repository.ChatMessageRepository;
import com.example.knot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatService(ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Persists a chat message and returns a response DTO.
     */
    public ChatMessageResponse sendMessage(UUID senderId, ChatMessageRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(request.getContent())
                .build();

        ChatMessage saved = chatMessageRepository.save(message);

        return toResponse(saved);
    }

    /**
     * Returns the full conversation between two users in chronological order.
     */
    public List<ChatMessageResponse> getConversation(UUID userId1, UUID userId2) {
        List<ChatMessage> messages = chatMessageRepository.findConversation(userId1, userId2);
        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns all messages received by a user, most recent first.
     */
    public List<ChatMessageResponse> getInbox(UUID recipientId) {
        List<ChatMessage> messages = chatMessageRepository
                .findByRecipientIdOrderByTimestampDesc(recipientId);
        return messages.stream()
                .map(this::toResponse)
                .toList();
    }

    private ChatMessageResponse toResponse(ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .recipientId(message.getRecipient().getId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}
