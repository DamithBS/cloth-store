package com.example.clothes_store.Service;

import com.example.clothes_store.Model.Entity.Order;
import com.example.clothes_store.Model.Entity.User;

public interface EmailService {

    void sendOrderConfirmationEmail(User user, Order order);
}
