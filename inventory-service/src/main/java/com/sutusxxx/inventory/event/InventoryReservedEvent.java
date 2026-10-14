package com.sutusxxx.inventory.event;

import java.math.BigDecimal;

public record InventoryReservedEvent(
        Long orderId,
        Long customerId,
        BigDecimal totalAmount
) {
}
