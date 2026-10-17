package com.sutusxxx.inventory;

import com.rabbitmq.client.Channel;
import com.sutusxxx.inventory.configuration.RabbitConfig;
import com.sutusxxx.inventory.event.InventoryRejectedEvent;
import com.sutusxxx.inventory.event.InventoryReservedEvent;
import com.sutusxxx.inventory.event.OrderCreatedEvent;
import com.sutusxxx.inventory.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.ORDER_CREATED_QUEUE)
    public void handle(OrderCreatedEvent event, Channel channel,
                       @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            process(event);
            channel.basicAck(tag, false);
        } catch (Exception exception) {
            channel.basicNack(tag, false, false);
        }
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_FAILED_QUEUE)
    public void handle(PaymentFailedEvent event) {
        log.info("Releasing stock for order {}. Reason: {}", event.orderId(), event.reason());
    }

    private void process(OrderCreatedEvent event) {
        boolean inStock = event.items().stream().noneMatch(i -> i.sku().startsWith("OOS"));

        if (inStock) {
            log.info("Stock reserved for order {}", event.orderId());
            rabbitTemplate.convertAndSend("inventory.exchange", "inventory.reserved",
                    new InventoryReservedEvent(event.orderId(), event.customerId(), event.totalAmount()));
        } else {
            log.info("Out of stock for order {}", event.orderId());
            rabbitTemplate.convertAndSend("inventory.exchange", "inventory.rejected",
                    new InventoryRejectedEvent(event.orderId(), "OUT_OF_STOCK"));
        }
    }
}
