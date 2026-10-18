package com.sutusxxx.payment;

import com.sutusxxx.payment.configuration.RabbitConfig;
import com.sutusxxx.payment.event.InventoryReservedEvent;
import com.sutusxxx.payment.event.PaymentCompletedEvent;
import com.sutusxxx.payment.event.PaymentFailedEvent;
import com.sutusxxx.payment.repository.ProcessedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final RabbitTemplate rabbitTemplate;
    private final ProcessedRepository processedRepository;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handle(InventoryReservedEvent event, @Header(AmqpHeaders.MESSAGE_ID) String messageId) throws InterruptedException {
        processOnce(messageId, event);
    }

    @Transactional
    public void processOnce(String messageId, InventoryReservedEvent event) throws InterruptedException {
        log.info("Processing payment for order {}", event.orderId());
        if (processedRepository.existsByMessageId(messageId)) {
            log.info("Duplicate message {}, skipping", messageId);
            return;
        }
        processedRepository.save(ProcessedEvent.from(messageId));

        if (event.totalAmount().compareTo(new BigDecimal("666")) == 0) {
            throw new IllegalStateException("Payment gateway unavailable");
        }
        // Simulation: Reject order if the total amount is more than 1000
        Thread.sleep(3000);
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
