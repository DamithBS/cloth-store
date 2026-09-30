package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.PaymentRequest;
import com.example.clothes_store.Controller.DTO.Response.PaymentResponse;

public interface PaymentService {

    void makePayment(String userName, Long orderId, PaymentRequest paymentRequest);

    PaymentResponse getPayment(String userName, Long orderID);


}
