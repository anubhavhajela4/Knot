package com.example.knot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {

    @NotBlank
    String name;

    @Email
    @NotBlank
    String email;

    @NotBlank
    String password;

    String bio;

}
