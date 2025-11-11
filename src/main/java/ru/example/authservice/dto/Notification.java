package ru.example.authservice.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record Notification(String to,
                           String templateType,
                           String recipientType,
                           Map<String, String> data) {
}
