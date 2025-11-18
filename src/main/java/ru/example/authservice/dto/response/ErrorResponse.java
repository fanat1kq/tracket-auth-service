package ru.example.authservice.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
    String message,
    String errorCode,
    int status,
    LocalDateTime timestamp

) {

    public ErrorResponse(String message, String errorCode, int status) {
        this(message, errorCode, status, LocalDateTime.now());
    }
}