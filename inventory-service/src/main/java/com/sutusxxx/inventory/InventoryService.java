package com.sutusxxx.inventory;

import com.sutusxxx.inventory.event.InventoryRejectedEvent;
import com.sutusxxx.inventory.event.InventoryReservedEvent;
import com.sutusxxx.inventory.event.OrderCreatedEvent;
import com.sutusxxx.inventory.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final ObjectMapper objectMapper;
    private final EventPublisher publisher;

    @KafkaListener(topics = "order.created")
    public void onOrderCreated(String payload) {
        OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
        boolean inStock = event.items().stream().noneMatch(i -> i.sku().startsWith("OOS"));

        if (inStock) {
            log.info("Stock reserved for order {}", event.orderId());
            publisher.publish("inventory.reserved", event.orderId(),
                    new InventoryReservedEvent(event.orderId(), event.customerId(), event.totalAmount()));
        } else {
            log.info("Out of stock for order {}", event.orderId());
            publisher.publish("inventory.rejected", event.orderId(),
                    new InventoryRejectedEvent(event.orderId(), "OUT_OF_STOCK"));
        }
    }

    @KafkaListener(topics = "payment.failed")
    public void onPaymentFailed(String payload) {
        PaymentFailedEvent event = objectMapper.readValue(payload, PaymentFailedEvent.class);
        log.info("Releasing stock for order {}. Reason: {}", event.orderId(), event.reason());
    }
}
