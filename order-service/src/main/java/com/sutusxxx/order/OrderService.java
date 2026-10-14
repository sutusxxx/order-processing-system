package com.sutusxxx.order;

import com.sutusxxx.order.configuration.RabbitConfig;
import com.sutusxxx.order.event.OrderCancelledEvent;
import com.sutusxxx.order.request.CreateOrderRequest;
import com.sutusxxx.order.event.OrderConfirmedEvent;
import com.sutusxxx.order.event.OrderCreatedEvent;
import com.sutusxxx.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final RabbitTemplate rabbitTemplate;
    private final OrderRepository orderRepository;

    public Long create(CreateOrderRequest request) {
        Order order = Order.from(request);
        orderRepository.save(order);

        log.info("Order {} created", order.getId());
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getCustomerId(),
                order.getItems().stream()
                        .map(item -> new OrderCreatedEvent.Item(item.getSku(), item.getQuantity()))
                        .toList(),
                order.getTotalAmount(),
                Instant.now()
        );
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, "order.created", event,
                new CorrelationData(event.orderId().toString()));

        return order.getId();
    }

    public void confirm(Long orderId) {
        log.info("Order {} confirmed", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow();

        if (order.getStatus() != OrderStatus.CREATED) return;
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(order);
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, "order.confirmed",
                new OrderConfirmedEvent(orderId, order.getCustomerId()));
    }

    public void cancel(Long orderId, String reason) {
        log.info("Order {} cancelled. Reason: {}", orderId, reason);
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getStatus() != OrderStatus.CREATED) return;
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, "order.cancelled",
                new OrderCancelledEvent(orderId, reason));
    }
}
