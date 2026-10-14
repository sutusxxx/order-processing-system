package com.sutusxxx.notification;

import com.sutusxxx.notification.configuration.RabbitConfig;
import com.sutusxxx.notification.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void process(OrderCreatedEvent event) {
        System.out.printf(
                "Notification sent to customer '%d', about order '%d'\n",
                event.customerId(),
                event.orderId()
        );
    }
}
