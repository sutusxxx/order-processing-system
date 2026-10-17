package com.sutusxxx.order;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;    // message_id
    private Broker broker;
    private String exchange;
    private String routingKey;
    private String payload;
    private OutboxStatus status;
    Instant createdAt;

    public void markSent() {
        status = OutboxStatus.SENT;
    }

    public static OutboxEvent rabbit(String exchange, String routingKey, String payload) {
        var outboxEvent = new OutboxEvent();
        outboxEvent.setBroker(Broker.RABBIT);
        outboxEvent.setExchange(exchange);
        outboxEvent.setRoutingKey(routingKey);
        outboxEvent.setPayload(payload);
        outboxEvent.setStatus(OutboxStatus.PENDING);
        outboxEvent.setCreatedAt(Instant.now());
        return outboxEvent;
    }

    public static OutboxEvent kafka(String topic, String messageKey, String payload) {
        var outboxEvent = new OutboxEvent();
        outboxEvent.setBroker(Broker.KAFKA);
        outboxEvent.setExchange(topic);
        outboxEvent.setRoutingKey(messageKey);
        outboxEvent.setPayload(payload);
        outboxEvent.setStatus(OutboxStatus.PENDING);
        outboxEvent.setCreatedAt(Instant.now());
        return outboxEvent;
    }
}
