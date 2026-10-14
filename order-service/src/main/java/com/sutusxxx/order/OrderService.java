package com.sutusxxx.order;

import com.sutusxxx.order.dto.CreateOrderDTO;
import com.sutusxxx.order.event.OrderCreatedEvent;
import com.sutusxxx.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderService {
    private final OrderPublisher publisher;
    private final OrderRepository orderRepository;

    public OrderService(
            OrderPublisher publisher,
            OrderRepository orderRepository) {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
    }

    public Long create(CreateOrderDTO dto) {
        Order order = Order.from(dto);
        orderRepository.save(order);

        System.out.printf("Order created with id '%d'\n", order.getId());
        publisher.publishOrderCreated(new OrderCreatedEvent(
                order.getId(),
                order.getCustomerId(),
                order.getTotal(),
                Instant.now()
        ));

        return order.getId();
    }
}
