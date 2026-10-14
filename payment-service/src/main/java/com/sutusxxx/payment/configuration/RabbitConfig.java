package com.sutusxxx.payment.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE = "payment.inventory-reserved.queue";

    @Bean
    TopicExchange inventoryExchange() {
        return new TopicExchange("inventory.exchange", true, false);
    }

    @Bean
    TopicExchange paymentExchange() {
        return new TopicExchange("payment.exchange", true, false);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(paymentQueue()).to(inventoryExchange()).with("inventory.reserved");
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange("dlx.exchange", true, false);
    }

    @Bean
    Queue paymentDlq() {
        return QueueBuilder.durable("payment.inventory-reserved.dlq").build();
    }

    @Bean
    Binding paymentDlqBinding() {
        return BindingBuilder.bind(paymentDlq()).to(deadLetterExchange()).with("payment.inventory-reserved");
    }

    @Bean
    Queue paymentQueue() {
        return QueueBuilder.durable(QUEUE)
                .deadLetterExchange("dlx.exchange")
                .deadLetterRoutingKey("payment.inventory-reserved")
                .build();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
