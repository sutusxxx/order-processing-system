package com.sutusxxx.notification;

import com.sutusxxx.notification.configuration.RabbitConfig;
import com.sutusxxx.notification.event.OrderCancelledEvent;
import com.sutusxxx.notification.event.OrderConfirmedEvent;
import com.sutusxxx.notification.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handle(OrderCreatedEvent event) {
        log.info("Sending notification about created order {}", event.orderId());
    }

    @RabbitListener(queues = RabbitConfig.CONFIRMED_QUEUE)
    public void handle(OrderConfirmedEvent event) {
        log.info("Sending notification about confirmed order {}", event.orderId());
    }

    @RabbitListener(queues = RabbitConfig.CANCELLED_QUEUE)
    public void handle(OrderCancelledEvent event) {
        log.info("Sending notification about cancelled order {}. Reason: {}", event.orderId(), event.reason());
    }
}
