package com.sutusxxx.order;

import com.sutusxxx.order.event.InventoryRejectedEvent;
import com.sutusxxx.order.event.PaymentCompletedEvent;
import com.sutusxxx.order.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OrderSagaListener {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment.completed")
    public void onPaymentCompleted(String payload) {
        orderService.confirm(objectMapper.readValue(payload, PaymentCompletedEvent.class).orderId());
    }

    @KafkaListener(topics = "payment.failed")
    public void onPaymentFailed(String payload) {
        PaymentFailedEvent event = objectMapper.readValue(payload, PaymentFailedEvent.class);
        orderService.cancel(event.orderId(), event.reason());
    }

    @KafkaListener(topics = "inventory.rejected")
    public void onInventoryRejected(String payload) {
        InventoryRejectedEvent event = objectMapper.readValue(payload, InventoryRejectedEvent.class);
        orderService.cancel(event.orderId(), event.reason());
    }
}
