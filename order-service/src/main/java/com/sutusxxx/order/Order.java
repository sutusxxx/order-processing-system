package com.sutusxxx.order;

import com.sutusxxx.order.request.CreateOrderRequest;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long customerId;

    @OneToMany(mappedBy = "order")
    private List<Item> items;

    private BigDecimal totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    public static Order from(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setTotalAmount(request.totalAmount());
        order.setItems(request.items().stream().map(Item::from).toList());
        return order;
    }
}
