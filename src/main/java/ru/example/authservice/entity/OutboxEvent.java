package ru.example.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;

          @Column(nullable = false)
          private String eventType;

          @Column(columnDefinition = "jsonb", nullable = false)
          private String payload;

          @Column(nullable = false)
          private LocalDateTime createdAt = LocalDateTime.now();

          @Column(nullable = false)
          private boolean processed = false;
}