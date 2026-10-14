package com.sutusxxx.order.request;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
    Long customerId,
    List<Item> items,
    BigDecimal totalAmount
) {
    public record Item(String sku, int quantity) {}
}
