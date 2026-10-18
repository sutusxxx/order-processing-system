package com.sutusxxx.payment;

import com.sutusxxx.payment.event.InventoryReservedEvent;
import com.sutusxxx.payment.event.PaymentCompletedEvent;
import com.sutusxxx.payment.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final ObjectMapper objectMapper;
    private final EventPublisher publisher;

    @KafkaListener(topics = "inventory.reserved")
    public void onInventoryReserved(String payload) throws InterruptedException {
        InventoryReservedEvent event = objectMapper.readValue(payload, InventoryReservedEvent.class);
        log.info("Processing payment for order {}", event.orderId());

        if (event.totalAmount().compareTo(new BigDecimal("666")) == 0) {
            throw new IllegalStateException("Payment gateway unavailable");
        }
        // Simulation: Reject order if the total amount is more than 1000
        Thread.sleep(3000);
        if (event.totalAmount().compareTo(new BigDecimal("1000")) > 0) {
            log.info("Payment failed for order {}", event.orderId());
            publisher.publish( "payment.failed", event.orderId(),
                    new PaymentFailedEvent(event.orderId(), "INSUFFICIENT_FUNDS"));
        } else {
            log.info("Payment completed for order {}", event.orderId());
            publisher.publish("payment.completed", event.orderId(),
                    new PaymentCompletedEvent(event.orderId(), 111L));
        }
    }
}
