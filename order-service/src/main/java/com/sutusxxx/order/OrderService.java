package com.sutusxxx.order;

import com.sutusxxx.contracts.OrderCreatedEvent;
import com.sutusxxx.order.dto.CreateOrderDTO;
import com.sutusxxx.order.repository.OrderItemRepository;
import com.sutusxxx.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderPublisher publisher;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(
            OrderPublisher publisher,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public void create(CreateOrderDTO dto) {
        Order order = new Order();
        List<OrderItem> items = orderItemRepository.findAllById(dto.itemIds());
        order.setItems(items);
        orderRepository.save(order);
        publisher.publish(new OrderCreatedEvent(
                order.getId()
        ));
    }
}
