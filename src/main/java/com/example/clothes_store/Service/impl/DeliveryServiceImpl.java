package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Response.DeliveryResponse;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.Delivery;
import com.example.clothes_store.Model.Entity.Order;
import com.example.clothes_store.Model.Eum.DeliveryStatus;
import com.example.clothes_store.Model.Eum.OrderStatus;
import com.example.clothes_store.Repository.DeliveryRepository;
import com.example.clothes_store.Repository.OrderRepository;
import com.example.clothes_store.Service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;


    // CREATE DELIVERY
    @Override
    @Transactional
    public void createDelivery(Order order){

        // Check whether delivery already exists
        if (deliveryRepository.findByOrderId(order.getId()).isPresent()){
            throw new IllegalStateException("Delivery already exists for this order");

        }

        //Create a new Delivery object
        Delivery delivery = new Delivery();

        // Connect this Delivery with the given Order
        delivery.setOrder(order);

        // Generate unique tracking number
        delivery.setTrackingNumber(generateTrackingNumber());

        //created delivery starts with PROCESSING
        delivery.setDeliveryStatus(DeliveryStatus.PROCESSING);

        //Save the Delivery into database
        deliveryRepository.save(delivery);

    }


    // GET DELIVERY BY ORDER ID
    @Override
    @Transactional(readOnly = true)
    public DeliveryResponse getDeliveryByOrderId(Long orderId, String userName){

        // Find the order using Order ID and authenticated user
        // This prevents one user from viewing another user's order.
        Order order = orderRepository.findByIdAndUserUserName(orderId,userName)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found for this user")
                );


        // Find the delivery associated with this order
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Delivery not found for order:" + orderId)
                );

        // Convert the Delivery Entity into DeliveryResponse DTO
        return mapToResponseDelivery(delivery);
    }


    // SHIP DELIVERY
    @Override
    @Transactional
    public void shipDelivery(Long orderId){

        // Find the delivery belonging to the given order
        Delivery delivery = findDelivery(orderId);

        // A delivery can be shipped ONLY when its current status
        if (delivery.getDeliveryStatus() != DeliveryStatus.PROCESSING){
            throw new IllegalStateException("Only PROCESSING delivery can be shipped");
        }

        // Change delivery status from PROCESSING to SHIPPED
        delivery.setDeliveryStatus(DeliveryStatus.SHIPPED);
        delivery.setShippedDate(LocalDateTime.now());

        // Save the updated delivery information
        deliveryRepository.save(delivery);
    }

    // MARK DELIVERY AS DELIVERED
    @Override
    @Transactional
    public void markAsDelivered(Long orderID){

        // Find the delivery belonging to the given order
        Delivery delivery = findDelivery(orderID);

        // A delivery can be marked as DELIVERED
        if (delivery.getDeliveryStatus() != DeliveryStatus.SHIPPED){
            throw  new IllegalStateException("Only SHIPPED delivery can be marked as DELIVERED");
        }

        // Change delivery status from SHIPPED to DELIVERED
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredDate(LocalDateTime.now());

        // Get the Order associated with this Delivery
        Order order = delivery.getOrder();

        // Delivery is completed,
        // therefore the Order should also be completed.
        order.setOrderStatus(OrderStatus.COMPLETED);

        // Save the updated Delivery and Order
        deliveryRepository.save(delivery);
        orderRepository.save(order);
    }


    // CANCEL DELIVERY
    @Override
    @Transactional
    public void cancelDelivery(Long orderId){

        // Find the delivery belonging to the given order
        Delivery delivery = findDelivery(orderId);

        // Delivery can be cancelled ONLY while it is still
        // in PROCESSING state.
        if (delivery.getDeliveryStatus() != DeliveryStatus.PROCESSING){
            throw  new IllegalStateException("Only PROCESSING delivery can be cancelled");
        }

        // Change Delivery status
        delivery.setDeliveryStatus(DeliveryStatus.CANCELLED);

        // Get the Order associated with this Delivery
        Order order = delivery.getOrder();

        // Since the delivery has been cancelled,
        // mark the related Order as CANCELLED as well.
        order.setOrderStatus(OrderStatus.CANCELLED);

        // Save the updated Delivery and Order
        deliveryRepository.save(delivery);
        orderRepository.save(order);
    }


    //---------------------------------------------
    // Helper methods


    // GENERATE TRACKING NUMBER
    private String generateTrackingNumber(){
        return "TRK" + UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();
    }

    // FIND DELIVERY
    private Delivery findDelivery(Long orderId){
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Delivery not found for order")
                );
    }

    // ENTITY -> DTO MAPPING
    private DeliveryResponse mapToResponseDelivery(Delivery delivery){

        // Create an empty DeliveryResponse DTO
        DeliveryResponse deliveryResponse = new DeliveryResponse();

        deliveryResponse.setId(delivery.getId());
        deliveryResponse.setOrderId(delivery.getOrder().getId());
        deliveryResponse.setTrackingNumber(delivery.getTrackingNumber());
        deliveryResponse.setShippedDate(delivery.getShippedDate());
        deliveryResponse.setDeliveredDate(delivery.getDeliveredDate());
        deliveryResponse.setDeliveryStatus(delivery.getDeliveryStatus());

        return deliveryResponse;

    }
}
