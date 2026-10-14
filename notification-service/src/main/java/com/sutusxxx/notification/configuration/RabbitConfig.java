package com.sutusxxx.notification.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE = "notification.order-created.queue";
    public static final String CONFIRMED_QUEUE = "notification.order-confirmed.queue";
    public static final String CANCELLED_QUEUE = "notification.order-cancelled.queue";

    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange("order.exchange", true, false);
    }

    @Bean
    Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Queue confirmedQueue() {
        return QueueBuilder.durable(CONFIRMED_QUEUE).build();
    }

    @Bean
    Queue cancelledQueue() {
        return QueueBuilder.durable(CANCELLED_QUEUE).build();
    }

    @Bean
    Binding confirmedBinding() {
        return BindingBuilder.bind(confirmedQueue()).to(orderExchange()).with("order.confirmed");
    }

    @Bean
    Binding cancelledBinding() {
        return BindingBuilder.bind(cancelledQueue()).to(orderExchange()).with("order.cancelled");
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(notificationQueue()).to(orderExchange()).with("order.created");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
