package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Response.DeliveryResponse;
import com.example.clothes_store.Model.Entity.Order;

public interface DeliveryService {

    void createDelivery(Order order);

    DeliveryResponse getDeliveryByOrderId(Long orderId, String userName);

    void shipDelivery(Long orderId);

    void markAsDelivered(Long orderId);

    void cancelDelivery(Long orderId);

}
