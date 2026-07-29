package com.sutusxxx.order.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "orders.exchange";

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    Queue inventoryQueue() {
        return new Queue("inventory.queue");
    }

    @Bean
    Queue notificationQueue() {
        return new Queue("notification.queue");
    }

    @Bean
    Binding inventoryBinding(
            Queue inventoryQueue,
            FanoutExchange exchange) {

        return BindingBuilder
                .bind(inventoryQueue)
                .to(exchange);
    }

    @Bean
    Binding notificationBinding(
            Queue notificationQueue,
            FanoutExchange exchange) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(exchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}