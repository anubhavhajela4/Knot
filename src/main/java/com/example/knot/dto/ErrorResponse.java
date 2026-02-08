package com.example.knot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    public String message;
    public LocalDateTime timestamp;

}
