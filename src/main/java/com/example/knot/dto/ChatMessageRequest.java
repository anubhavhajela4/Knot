package com.example.knot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessageRequest {

    @NotBlank(message = "Message content cannot be blank")
    private String content;

    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;
}
