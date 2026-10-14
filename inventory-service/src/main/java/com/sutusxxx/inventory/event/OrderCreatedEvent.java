package com.sutusxxx.inventory.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        List<Item> items,
        BigDecimal totalAmount,
        Instant createdAt
) {
    public record Item(String sku, int quantity) {}
}
