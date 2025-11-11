package ru.example.authservice.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
          String message,
          LocalDateTime timestamp
) {
          public ErrorResponse {
                    timestamp = timestamp != null ? timestamp : LocalDateTime.now();
          }

          public ErrorResponse(String message) {
                    this(message, LocalDateTime.now());
          }
}