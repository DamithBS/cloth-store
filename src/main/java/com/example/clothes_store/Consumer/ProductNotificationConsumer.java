package com.example.clothes_store.Consumer;

import com.example.clothes_store.Config.FanoutRabbitMQConfig;
import com.example.clothes_store.Event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductNotificationConsumer {

    // Listen for product-created events from the notification queue.
    @RabbitListener(queues = FanoutRabbitMQConfig.PRODUCT_NOTIFICATION_QUEUE)
    public void productCreateNotificationConsumer(ProductCreatedEvent productCreatedEvent){

        log.info("---Consumer Notification added New product....");
    }
}
