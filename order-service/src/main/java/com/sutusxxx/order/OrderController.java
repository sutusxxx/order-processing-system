package com.sutusxxx.order;

import com.sutusxxx.order.dto.CreateOrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateOrderDTO body) {
        orderService.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
