package com.sutusxxx;

import com.sutusxxx.contracts.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @RabbitListener(queues = "notification.queue")
    public void process(OrderCreatedEvent event) {
        System.out.printf(
                "Notification sent to customer '%d', about order '%d'\n",
                event.customerId(),
                event.orderId()
        );
    }
}
