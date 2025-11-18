package ru.example.authservice.dto.enumerates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EventType {
    USER_REGISTERED("USER_REGISTERED");

    private final String eventTypeName;

    public static EventType fromString(String type) {
        return Arrays.stream(values())
            .filter(typeFromEnum -> typeFromEnum.name().equals(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown event type: " + type));
    }
}
