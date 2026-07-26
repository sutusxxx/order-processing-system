package com.sutusxxx.order.dto;

import java.util.List;

public record CreateOrderDTO(
        List<Long> itemIds
) {
}
