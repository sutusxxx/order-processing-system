package com.sutusxxx.order.event;

public record OrderCancelledEvent(
        Long orderId,
        String reason
) {
}
