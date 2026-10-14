package com.sutusxxx.order;

import com.sutusxxx.order.request.CreateOrderRequest;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String sku;

    private int quantity;

    public static Item from(CreateOrderRequest.Item request) {
        Item item = new Item();
        item.setSku(request.sku());
        item.setQuantity(request.quantity());
        return item;
    }
}
