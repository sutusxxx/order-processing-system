package com.sutusxxx.inventory;

import com.sutusxxx.contracts.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    @RabbitListener(queues = "inventory.queue")
    public void process(OrderCreatedEvent event) {
        System.out.printf("Inventory checked for order '%d'...\n", event.orderId());
    }
}
