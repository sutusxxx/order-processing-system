package com.sutusxxx.inventory.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String ORDER_CREATED_QUEUE = "inventory.order-created.queue";
    public static final String PAYMENT_FAILED_QUEUE = "inventory.payment-failed.queue";

    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange("order.exchange", true, false);
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
    Queue inventoryQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE).build();
    }

    @Bean
    Queue
    paymentFailedQueue() {
        return QueueBuilder.durable(PAYMENT_FAILED_QUEUE).build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(inventoryQueue()).to(orderExchange()).with("order.created");
    }

    @Bean
    Binding paymentFailedBinding() {
        return BindingBuilder.bind(paymentFailedQueue()).to(paymentExchange()).with("payment.failed");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
