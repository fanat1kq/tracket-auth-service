package ru.example.authservice.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record UserRegisteredPayload(
    Long userId,
    String email,
    String username,
    String templateType,
    String recipientType,
    Map<String, String> data
) {
}