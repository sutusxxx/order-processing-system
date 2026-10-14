package com.sutusxxx.inventory.event;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        BigDecimal total,
        Instant createdAt
) {
}
