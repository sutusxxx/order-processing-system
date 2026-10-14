package com.sutusxxx.order.event;

public record PaymentCompletedEvent(
        Long orderId,
        Long paymentId
) {
}
