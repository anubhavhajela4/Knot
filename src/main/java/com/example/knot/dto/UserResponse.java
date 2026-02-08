package com.example.knot.dto;

import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private String bio;
    private LocalDateTime createdAt;
}
