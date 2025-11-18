package ru.example.authservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;

    private String tokenType;

    private Long userId;

    private String username;

    private String email;

    private String name;

    private String role;

    private String department;

    private Integer expiresIn;
}

