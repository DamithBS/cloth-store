package com.example.clothes_store.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutRabbitMQConfig {

    // Defines the name of the Fanout Exchange used for product-related events.
    public static final String PRODUCT_EXCHANGE= "product.fanout.exchange";

    // Defines the queue name for processing product-related email notifications.
    public static final String PRODUCT_EMAIL_QUEUE = "product.email.queue";

    // Defines the queue name for processing product-related application notifications.
    public static final String PRODUCT_NOTIFICATION_QUEUE ="product.notification.queue";


    // Creates and registers a Fanout Exchange as a Spring bean
    @Bean
    public FanoutExchange productFanoutExchange(){
        return new FanoutExchange(PRODUCT_EXCHANGE,true,false);
    }

    // Creates and registers the product email,notification queue as a Spring bean.
    @Bean
    public Queue productEmailQueue(){
        return new Queue(PRODUCT_EMAIL_QUEUE,true);
    }

    @Bean
    public Queue productNotificationQueue(){
        return new Queue(PRODUCT_NOTIFICATION_QUEUE,true);
    }


    // Creates a binding between the product email,notification queue and the Fanout Exchange.
    @Bean
    public Binding ProductEmailBinding(
            Queue productEmailQueue,
            FanoutExchange productFanoutExchange
    ){
        return BindingBuilder
                .bind(productEmailQueue)
                .to(productFanoutExchange);
    }

    //Binding
    @Bean
    public Binding productNotificationBinding(
            Queue productNotificationQueue,
            FanoutExchange productFanoutExchange
    ){
        return BindingBuilder
                .bind(productNotificationQueue)
                .to(productFanoutExchange);
    }



}
