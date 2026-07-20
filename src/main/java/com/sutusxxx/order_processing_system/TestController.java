package com.sutusxxx.order_processing_system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final MessageProducer producer;

    public TestController(MessageProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/send")
    public String send() {
        producer.send("Hello RabbitMQ!");
        return "Sent";
    }
}
