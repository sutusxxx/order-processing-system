package com.sutusxxx.order;

import com.sutusxxx.contracts.OrderCreatedEvent;
import com.sutusxxx.order.dto.CreateOrderDTO;
import com.sutusxxx.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

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

    public void create(CreateOrderDTO dto) {
        Order order = Order.from(dto);
        orderRepository.save(order);
        publisher.publish(new OrderCreatedEvent(
                order.getId(),
                order.getCustomerId(),
                order.getTotal()
        ));
    }
}
