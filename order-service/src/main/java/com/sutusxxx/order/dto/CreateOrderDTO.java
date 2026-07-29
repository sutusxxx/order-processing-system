package com.sutusxxx.order.dto;

import java.math.BigDecimal;

public record CreateOrderDTO(
    Long customerId,
    BigDecimal total
) {
}
