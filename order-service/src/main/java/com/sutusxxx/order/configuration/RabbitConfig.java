package com.sutusxxx.order.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String INVENTORY_REJECTED_QUEUE = "order.inventory-rejected.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "order.payment-completed.queue";
    public static final String PAYMENT_FAILED_QUEUE = "order.payment-failed.queue";

    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE, true, false);
    }

    @Bean
    TopicExchange inventoryExchange() {
        return new TopicExchange("inventory.exchange", true, false);
    }

    @Bean
    TopicExchange paymentExchange() {
        return new TopicExchange("payment.exchange", true, false);
    }

    @Bean
    Queue inventoryRejectedQueue() {
        return QueueBuilder.durable(INVENTORY_REJECTED_QUEUE).build();
    }

    @Bean
    Queue paymentCompletedQueue() {
        return QueueBuilder.durable(PAYMENT_COMPLETED_QUEUE).build();
    }

    @Bean
    Queue paymentFailedQueue() {
        return QueueBuilder.durable(PAYMENT_FAILED_QUEUE).build();
    }

    @Bean
    Binding inventoryRejectedBinding() {
        return BindingBuilder.bind(inventoryRejectedQueue()).to(inventoryExchange()).with("inventory.rejected");
    }

    @Bean
    Binding paymentCompletedBinding() {
        return BindingBuilder.bind(paymentCompletedQueue()).to(paymentExchange()).with("payment.completed");
    }

    @Bean
    Binding paymentFailedBinding() {
        return BindingBuilder.bind(paymentFailedQueue()).to(paymentExchange()).with("payment.failed");
    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}