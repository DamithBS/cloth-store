package com.example.clothes_store.Controller.DTO.Response;

import com.example.clothes_store.Model.Eum.DeliveryStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryResponse {

    private Long id;
    private Long orderId;
    private String trackingNumber;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
}
