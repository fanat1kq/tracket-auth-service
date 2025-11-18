package ru.example.authservice.dto;

import lombok.Builder;


@Builder
public record NotificationPayloadDTO(
    String to,
    String userId,
    String templateType,
    String recipientType,
    String eventTime
) {
}
