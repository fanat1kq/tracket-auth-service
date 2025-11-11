package ru.example.authservice.dto;

import lombok.Builder;

@Builder
public record LoginRequest(
          String password,
          String username
) {
}