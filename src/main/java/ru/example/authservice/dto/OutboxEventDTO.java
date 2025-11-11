package ru.example.authservice.dto;

import lombok.Builder;

@Builder
public record OutboxEventDTO(
          String eventType,
          String payload
) {
}
