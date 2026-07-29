package com.sutusxxx.contracts;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        BigDecimal total
) {
}
