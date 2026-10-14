package com.sutusxxx.inventory.event;

public record PaymentFailedEvent(
        Long orderId,
        String reason
) {
}
