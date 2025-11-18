package ru.example.authservice.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.example.authservice.dto.OutboxEventDTO;

@Mapper(componentModel = "spring")
public interface OutboxEventMapper {

    @Mapping(target = "eventType", expression = "java(getEventType(eventNode))")
    @Mapping(target = "payload", expression = "java(getPayload(eventNode))")
    OutboxEventDTO toOutboxEventDTO(JsonNode eventNode);

    default String getEventType(JsonNode eventNode) {
        return eventNode.has("event_type") ? eventNode.get("event_type").asText() :
            null;
    }

    default String getPayload(JsonNode eventNode) {
        return eventNode.has("payload") ? eventNode.get("payload").asText() : null;
    }
}
