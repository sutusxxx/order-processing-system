package com.sutusxxx.order.event;

public record OrderConfirmedEvent(
        Long orderId,
        Long customerId
) {
}
