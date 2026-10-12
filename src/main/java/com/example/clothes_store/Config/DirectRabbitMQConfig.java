package com.example.clothes_store.Config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;


@Configuration
public class DirectRabbitMQConfig {

    public static final String QUEUE_NAME = "order.created.queue";

    public static final String EMAIL_QUEUE = "order.email.queue";

    public static final String EXCHANGE_NAME = "order.exchange";

    public static final String ROUTING_KEY = "order.created";


    //Queues
    @Bean
    public Queue orderCreatedQueue(){
        return new Queue(QUEUE_NAME,true);
    }

    //Exchange
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(EXCHANGE_NAME);
    }

    //Queues
    @Bean
    public Queue emailQueue(){
        return new Queue(EMAIL_QUEUE,true);
    }


    //Bindings
    @Bean
    public Binding orderBinding(
            Queue orderCreatedQueue,
            DirectExchange orderExchange
    ){
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(orderExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding emailBinding(
            Queue emailQueue,
            DirectExchange orderExchange
    ){
        return BindingBuilder
                .bind(emailQueue)
                .to(orderExchange)
                .with(ROUTING_KEY);
    }



    //Object <-> JSON converter
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
