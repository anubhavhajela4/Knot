package com.example.knot.controller;

import com.example.knot.dto.ChatMessageRequest;
import com.example.knot.dto.ChatMessageResponse;
import com.example.knot.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService,
                          SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * STOMP message handler.
     * Client sends to: /app/chat.send
     * Recipient receives on: /user/{recipientId}/queue/messages
     */
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request,
                            Authentication authentication) {

        UUID senderId = (UUID) authentication.getPrincipal();

        ChatMessageResponse response = chatService.sendMessage(senderId, request);

        // Deliver the message to the recipient's personal queue
        messagingTemplate.convertAndSendToUser(
                request.getRecipientId().toString(),
                "/queue/messages",
                response
        );

        // Also send back to the sender so their UI updates
        messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/messages",
                response
        );
    }

    /**
     * REST endpoint to fetch message history between the authenticated user
     * and a specific recipient.
     *
     * GET /api/chat/{recipientId}
     */
    @GetMapping("/api/chat/{recipientId}")
    @ResponseBody
    public ResponseEntity<List<ChatMessageResponse>> getConversation(
            @PathVariable UUID recipientId,
            Authentication authentication) {

        UUID currentUserId = (UUID) authentication.getPrincipal();
        List<ChatMessageResponse> conversation =
                chatService.getConversation(currentUserId, recipientId);

        return ResponseEntity.ok(conversation);
    }

    /**
     * REST endpoint to fetch the authenticated user's inbox (received messages).
     *
     * GET /api/chat/inbox
     */
    @GetMapping("/api/chat/inbox")
    @ResponseBody
    public ResponseEntity<List<ChatMessageResponse>> getInbox(
            Authentication authentication) {

        UUID currentUserId = (UUID) authentication.getPrincipal();
        List<ChatMessageResponse> inbox = chatService.getInbox(currentUserId);

        return ResponseEntity.ok(inbox);
    }
}
