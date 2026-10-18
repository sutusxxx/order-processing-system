package com.sutusxxx.inventory;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void publish(String topic, Long orderId, Object event) {
        kafkaTemplate.send(topic, orderId.toString(), objectMapper.writeValueAsString(event));
    }
}
