package com.sutusxxx.order;

import com.sutusxxx.order.configuration.RabbitConfig;
import com.sutusxxx.order.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishOrderCreated(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, "order.created", event);
    }
}
