package com.example.knot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CircleResponse {
    private UUID id;
    private String name;
    private String description;
    private String createdBy;
    private int memberCount;
}
