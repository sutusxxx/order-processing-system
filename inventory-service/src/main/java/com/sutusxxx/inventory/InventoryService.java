package com.sutusxxx.inventory;

import com.sutusxxx.inventory.configuration.RabbitConfig;
import com.sutusxxx.inventory.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void process(OrderCreatedEvent event) {
        System.out.printf("Stock reservation for order '%d'...\n", event.orderId());
    }
}
