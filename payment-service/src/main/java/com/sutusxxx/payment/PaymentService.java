package com.sutusxxx.payment;

import com.sutusxxx.contracts.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @RabbitListener(queues = "payment.queue")
    public void process(OrderCreatedEvent event) {
        System.out.printf("Payment for order '%d' started...", event.orderId());
    }
}
