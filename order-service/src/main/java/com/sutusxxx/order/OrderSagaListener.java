package com.sutusxxx.order;

import com.sutusxxx.order.configuration.RabbitConfig;
import com.sutusxxx.order.event.InventoryRejectedEvent;
import com.sutusxxx.order.event.PaymentCompletedEvent;
import com.sutusxxx.order.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderSagaListener {
    private final OrderService orderService;

    @RabbitListener(queues = RabbitConfig.PAYMENT_COMPLETED_QUEUE)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        orderService.confirm(event.orderId());
    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_FAILED_QUEUE)
    public void onPaymentFailed(PaymentFailedEvent event) {
        orderService.cancel(event.orderId(), event.reason());
    }

    @RabbitListener(queues = RabbitConfig.INVENTORY_REJECTED_QUEUE)
    public void onInventoryRejected(InventoryRejectedEvent event) {
        orderService.cancel(event.orderId(), event.reason());
    }
}
