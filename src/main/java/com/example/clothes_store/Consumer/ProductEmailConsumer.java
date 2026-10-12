package com.example.clothes_store.Consumer;

import com.example.clothes_store.Config.FanoutRabbitMQConfig;
import com.example.clothes_store.Event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEmailConsumer {

    // Listen for product-created events from the email queue.
    @RabbitListener(queues = FanoutRabbitMQConfig.PRODUCT_EMAIL_QUEUE)
    public void productCreateConsumer(ProductCreatedEvent productCreatedEvent){

        log.info("----Consumer Email added New product ....");
    }
}
