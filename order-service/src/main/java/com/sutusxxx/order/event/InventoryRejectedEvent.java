package com.sutusxxx.order.event;

public record InventoryRejectedEvent(
        Long orderId,
        String reason
) {
}
