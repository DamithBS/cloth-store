package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.PaymentRequest;
import com.example.clothes_store.Controller.DTO.Response.PaymentResponse;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.Order;
import com.example.clothes_store.Model.Entity.Payment;
import com.example.clothes_store.Model.Entity.User;
import com.example.clothes_store.Model.Eum.OrderStatus;
import com.example.clothes_store.Model.Eum.PaymentMethod;
import com.example.clothes_store.Model.Eum.PaymentStatus;
import com.example.clothes_store.Repository.OrderRepository;
import com.example.clothes_store.Repository.PaymentRepository;
import com.example.clothes_store.Repository.UserRepository;
import com.example.clothes_store.Service.DeliveryService;
import com.example.clothes_store.Service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DeliveryService deliveryService;


    // payment pay in order items
    @Override
    @Transactional
    public void makePayment(String userName, Long orderId, PaymentRequest paymentRequest){

        // Find authenticated user
        User user= getUser(userName);

        // Find order
        Order order= findOrder(orderId);

        // Make sure order belongs to authenticated user
        if (!order.getUser().getId().equals(user.getId())){
            throw new ResourceNotFoundException("order not found");
        }

        // Check whether payment already exists
        if (paymentRepository.existsByOrderId(orderId)){
            throw new IllegalStateException("Payment already exists for this order");
        }

        // Validate order status
        if (order.getOrderStatus() != OrderStatus.PENDING){
            throw new IllegalStateException("Payment cannot be made for this order");
        }


        // Create Payment
        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setPaymentDate(LocalDateTime.now());

        //Generate transaction ID
        payment.setTransactionId("TXN"+ UUID.randomUUID());

        /*
         * 8. Process payment
         *
         * This is currently a simulated payment.
         *
         * Real payment gateway integration
         * will be added here later.
         */
        PaymentStatus paymentStatus = processPayment(paymentRequest.getPaymentMethod());

        // If payment successful or failed, confirm order
        if (paymentStatus == PaymentStatus.FAILED){
            throw new IllegalStateException("Payment failed. Please try again.");
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        deliveryService.createDelivery(order);
        orderRepository.save(order);

        // Save payment
        paymentRepository.save(payment);

    }


    // get all details from payment order
    @Override
    @Transactional
    public PaymentResponse getPayment(String userName,Long orderId){

        // Find authenticated user
        User user = getUser(userName);

        // Find order
        Order order = findOrder(orderId);

        // Check order ownership
        if (!order.getUser().getId().equals(user.getId())){
            throw new ResourceNotFoundException("order not found");
        }

        // Find payment
        Payment payment =paymentRepository.findByOrderId(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment not found for this order"));

        return mapToPaymentResponse(payment);
    }



    //---------------------------------------------------
    // Helper methods

    // Find authenticated user
   private User getUser(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("Authenticated user not found")
                );
   }

    // Find order
   private Order findOrder(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("order not found")
                );

   }

    // Payment Processing
   private PaymentStatus processPayment(PaymentMethod paymentMethod){
        if (paymentMethod ==null){
            return PaymentStatus.FAILED;
        }
        return PaymentStatus.SUCCESS;
   }


   // MAP PAYMENT -> RESPONSE
   private PaymentResponse mapToPaymentResponse(Payment payment){
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .build();
   }

}
