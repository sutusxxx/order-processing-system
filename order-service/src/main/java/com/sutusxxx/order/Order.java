package com.sutusxxx.order;

import com.sutusxxx.order.dto.CreateOrderDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long customerId;

    private BigDecimal total;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    public static Order from(CreateOrderDTO dto) {
        Order order = new Order();
        order.setCustomerId(dto.customerId());
        order.setTotal(dto.total());
        return order;
    }
}
