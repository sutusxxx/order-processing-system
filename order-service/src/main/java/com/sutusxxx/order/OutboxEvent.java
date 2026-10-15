package com.sutusxxx.order;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String exchange;
    private String routingKey;
    private String payload;
    private OutboxStatus status;
    Instant createdAt;

    public void markSent() {
        status = OutboxStatus.SENT;
    }

    public static OutboxEvent from(String exchange, String routingKey, String payload) {
        var outboxEvent = new OutboxEvent();
        outboxEvent.setExchange(exchange);
        outboxEvent.setRoutingKey(routingKey);
        outboxEvent.setPayload(payload);
        outboxEvent.setStatus(OutboxStatus.PENDING);
        outboxEvent.setCreatedAt(Instant.now());
        return outboxEvent;
    }
}
