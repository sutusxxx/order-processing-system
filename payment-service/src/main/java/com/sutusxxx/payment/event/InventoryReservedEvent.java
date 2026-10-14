package com.sutusxxx.payment.event;

import java.math.BigDecimal;

public record InventoryReservedEvent(
        Long orderId,
        Long customerId,
        BigDecimal totalAmount
) {
}
