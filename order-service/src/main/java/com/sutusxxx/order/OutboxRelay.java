package com.sutusxxx.order;

import com.sutusxxx.order.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay =  1000)
    public void publishPending() {
        for (OutboxEvent e : outboxRepository.findTop100ByStatusOrderByCreatedAt(OutboxStatus.PENDING)) {
            try {
                if (e.getBroker() == Broker.KAFKA) sendKafka(e);;
                e.markSent();
                outboxRepository.save(e);
            } catch (Exception exception) {
                log.warn("Outbox publish failed, retrying next tick: {}", e.getId(), exception);
                return;
            }
        }
    }

    private void sendKafka(OutboxEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        var record = new ProducerRecord<>(event.getExchange(), event.getRoutingKey(), event.getPayload());
        record.headers().add("messageId", event.getId().toString().getBytes(StandardCharsets.UTF_8));
        kafkaTemplate.send(record).get(5, TimeUnit.SECONDS);
    }
}
