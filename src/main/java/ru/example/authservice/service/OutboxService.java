package ru.example.authservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.authservice.entity.OutboxEvent;
import ru.example.authservice.repository.OutboxEventRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Transactional
    public void createEvent(String eventType, Object payload) {
        String payloadJson = objectMapper.writeValueAsString(payload);
        outboxEventRepository.save(OutboxEvent.builder()
            .eventType(eventType)
            .payload(payloadJson)
            .deduplicationKey(String.valueOf(UUID.randomUUID()))
            .createdAt(LocalDateTime.now())
            .processed(false)
            .build());
    }
}
