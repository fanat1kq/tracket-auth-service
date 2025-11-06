package ru.example.authservice.publisher;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.example.authservice.config.KafkaTopicsProperties;
import ru.example.authservice.dto.Notification;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

          private final KafkaTemplate<String, Object> kafkaTemplate;

          private final KafkaTopicsProperties kafkaTopicsProperties;

          @SneakyThrows
          public void send(Notification dto) {
                    Message<Notification> emailMessage = MessageBuilder
                              .withPayload(dto)
                              .setHeader(KafkaHeaders.TOPIC, kafkaTopicsProperties.getTopics().getEmailSending())
                              .build();
                    log.info("EmailMessageRequestDto: Sending message to consumer = {}", dto);
                    kafkaTemplate.send(emailMessage).get();
          }
}
