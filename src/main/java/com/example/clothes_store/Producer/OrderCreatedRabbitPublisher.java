package com.example.clothes_store.Producer;

import com.example.clothes_store.Config.DirectRabbitMQConfig;
import com.example.clothes_store.Event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedRabbitPublisher {

    // Inject RabbitTemplate to publish messages to RabbitMQ.
    private final RabbitTemplate rabbitTemplate;

    // Publish the order-created event to the configured Direct Exchange.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent){

        // The exchange uses the specified routing key to route the message
        // to the queues bound with the matching routing key.
        rabbitTemplate.convertAndSend(
                DirectRabbitMQConfig.EXCHANGE_NAME,
                DirectRabbitMQConfig.ROUTING_KEY,
                orderCreatedEvent
        );

        log.info("Order created event published.... orderId={}, userId={}",
                orderCreatedEvent.orderId(),
                orderCreatedEvent.userId()
        );
    }
}
