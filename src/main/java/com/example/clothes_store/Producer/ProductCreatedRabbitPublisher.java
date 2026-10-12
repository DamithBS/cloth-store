package com.example.clothes_store.Producer;

import com.example.clothes_store.Config.DirectRabbitMQConfig;
import com.example.clothes_store.Config.FanoutRabbitMQConfig;
import com.example.clothes_store.Event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCreatedRabbitPublisher {

    // Inject RabbitTemplate to publish messages to RabbitMQ.
    private final RabbitTemplate rabbitTemplate;

    // Publish a product-created event to the Fanout Exchange.
    public void publishProductCreated(ProductCreatedEvent productCreatedEvent){

        // Send the event to the Fanout Exchange.
        // The routing key is ignored by a Fanout Exchange.
        // All queues bound to this exchange receive a copy of the message.
        rabbitTemplate.convertAndSend(
                FanoutRabbitMQConfig.PRODUCT_EXCHANGE,
                "",
                productCreatedEvent
        );

        log.info("product create producer event.... productId{} ",productCreatedEvent.productId());

    }
}
