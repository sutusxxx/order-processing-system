package com.sutusxxx.inventory.event;

public record InventoryRejectedEvent(
        Long orderId,
        String reason
) {
}
