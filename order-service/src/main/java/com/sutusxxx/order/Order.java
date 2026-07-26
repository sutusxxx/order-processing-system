package com.sutusxxx.order;

import com.sutusxxx.order.dto.CreateOrderDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Order {
    @Id
    private Long id;

    @OneToMany
    private List<OrderItem> items = new ArrayList<>();

    public double calculateTotal() {
        // sums up item prices, applies tax etc..
        return 0;
    }
}
