package com.sutusxxx.payment.event;

public record PaymentFailedEvent(
        Long orderId,
        String reason
) {
}
