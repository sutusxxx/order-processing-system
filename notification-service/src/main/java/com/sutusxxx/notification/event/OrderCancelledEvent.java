package com.sutusxxx.notification.event;

public record OrderCancelledEvent(
        Long orderId,
        String reason
) {
}
