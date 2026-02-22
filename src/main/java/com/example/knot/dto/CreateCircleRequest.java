package com.example.knot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCircleRequest {

    @NotBlank
    private String name;

    private String description;
}
