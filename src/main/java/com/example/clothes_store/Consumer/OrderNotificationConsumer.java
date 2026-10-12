package com.example.clothes_store.Consumer;

import com.example.clothes_store.Config.DirectRabbitMQConfig;
import com.example.clothes_store.Event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderNotificationConsumer {

    // Listen for order-created events from the notification queue.
    @RabbitListener(queues = DirectRabbitMQConfig.QUEUE_NAME)
    public void receiveOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent){

        log.info(
                "Order created event received. orderId={}, userId={}",
                orderCreatedEvent.orderId(),
                orderCreatedEvent.userId()
        );
    }
}
