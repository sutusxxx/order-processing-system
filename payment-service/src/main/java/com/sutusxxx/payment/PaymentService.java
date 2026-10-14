package com.sutusxxx.payment;

import com.sutusxxx.payment.configuration.RabbitConfig;
import com.sutusxxx.payment.event.InventoryReservedEvent;
import com.sutusxxx.payment.event.PaymentCompletedEvent;
import com.sutusxxx.payment.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handle(InventoryReservedEvent event) {
        // Simulation: Reject order if the total amount is more than 1000
        if (event.totalAmount().compareTo(new BigDecimal("1000")) > 0) {
            log.info("Payment failed for order {}", event.orderId());
            rabbitTemplate.convertAndSend("payment.exchange", "payment.failed",
                    new PaymentFailedEvent(event.orderId(), "INSUFFICIENT_FUNDS"));
        } else {
            log.info("Payment completed for order {}", event.orderId());
            rabbitTemplate.convertAndSend("payment.exchange", "payment.completed",
                    new PaymentCompletedEvent(event.orderId(), 111L));
        }
    }
}
