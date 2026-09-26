package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Response.OrderResponse;

import java.util.List;

public interface OrderService {

    void createOrder(String userName);

    void orderSingleItem(String userName, Long cartItemId);

    List<OrderResponse> getOrders(String userName);

}
