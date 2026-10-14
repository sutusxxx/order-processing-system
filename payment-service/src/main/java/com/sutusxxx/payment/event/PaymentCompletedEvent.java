package com.sutusxxx.payment.event;

public record PaymentCompletedEvent(
        Long orderId,
        Long paymentId
) {
}
