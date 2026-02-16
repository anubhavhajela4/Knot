package com.example.knot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class AuthResponse {

    private String message;

    private String token;
}
