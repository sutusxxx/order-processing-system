package com.sutusxxx.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
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
    Queue notificationQueue() {
        return new Queue("notification.queue");
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
