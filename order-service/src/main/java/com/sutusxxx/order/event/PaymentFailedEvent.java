package com.sutusxxx.order.event;

public record PaymentFailedEvent(
        Long orderId,
        String reason
) {
}
