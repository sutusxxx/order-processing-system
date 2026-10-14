package com.sutusxxx.payment;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublisherCallbacks {
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    void init() {
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if (!ack) log.error("Broker NACK: id={} cause={}", correlationData.getId(), cause);
        }));
        rabbitTemplate.setReturnsCallback(returned ->
                log.error("UNROUTABLE: exchange={} key={} reason={}",
                        returned.getExchange(), returned.getRoutingKey(), returned.getReplyText()));
    }
}
