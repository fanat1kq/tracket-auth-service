package ru.example.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.authservice.entity.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
}