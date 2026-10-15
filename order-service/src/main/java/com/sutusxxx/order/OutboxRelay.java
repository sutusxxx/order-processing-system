package com.sutusxxx.order;

import com.sutusxxx.order.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay =  1000)
    public void publishPending() {
        for (OutboxEvent e : outboxRepository.findTop100ByStatusOrderByCreatedAt(OutboxStatus.PENDING)) {
            try {
                Message msg = MessageBuilder.withBody(e.getPayload().getBytes(StandardCharsets.UTF_8))
                        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                        .setMessageId(e.getId().toString())
                        .build();

                rabbitTemplate.invoke(t -> {
                    t.send(e.getExchange(), e.getRoutingKey(), msg);
                    t.waitForConfirmsOrDie(5000);
                    return true;
                });

                e.markSent();
                outboxRepository.save(e);
            } catch (Exception exception) {
                log.warn("Outbox publish failed, retrying next tick: {}", e.getId(), exception);
                return;
            }
        }
    }
}
