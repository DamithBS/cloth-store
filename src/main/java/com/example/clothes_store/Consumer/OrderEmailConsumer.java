package com.example.clothes_store.Consumer;

import com.example.clothes_store.Config.DirectRabbitMQConfig;
import com.example.clothes_store.Event.OrderCreatedEvent;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.Order;
import com.example.clothes_store.Model.Entity.User;
import com.example.clothes_store.Repository.OrderRepository;
import com.example.clothes_store.Repository.UserRepository;
import com.example.clothes_store.Service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEmailConsumer {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Listen for order-created events from the order email queue.
    @RabbitListener(queues = DirectRabbitMQConfig.EMAIL_QUEUE)
    public void receiveOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent){

        log.info("Email event received. orderId={}",
                orderCreatedEvent.orderId()
        );


        // Retrieve the order together with its items.
        // Throw an exception if the order does not exist.
        Order order = orderRepository.findByIdWithItems(orderCreatedEvent.orderId())
                .orElseThrow(()-> new ResourceNotFoundException( "Order not found: " ));

        // Retrieve the user associated with the event.
        // Throw an exception if the user does not exist.
        User user = userRepository.findById(orderCreatedEvent.userId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found: "));

        // Send the order confirmation email to the user.
        emailService.sendOrderConfirmationEmail(user,order);

        log.info(
                "Email processing completed. orderId={}",
                orderCreatedEvent.orderId()
        );

    }
}
