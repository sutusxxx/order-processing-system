package com.sutusxxx.notification.event;

public record OrderConfirmedEvent(
        Long orderId,
        Long customerId
) {
}
