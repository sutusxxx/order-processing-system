package com.sutusxxx.payment;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "processed_events")
public class ProcessedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String messageId;

    private Instant createdAt;

    public static ProcessedEvent from(String messageId) {
        var event = new ProcessedEvent();
        event.setMessageId(messageId);
        event.setCreatedAt(Instant.now());
        return event;
    }
}
