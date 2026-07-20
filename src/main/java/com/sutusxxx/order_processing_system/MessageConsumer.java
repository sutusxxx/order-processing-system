package com.sutusxxx.order_processing_system;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @RabbitListener(queues = "my.queue")
    public void receive(String message) {
        System.out.println("Received: " + message);
    }
}
