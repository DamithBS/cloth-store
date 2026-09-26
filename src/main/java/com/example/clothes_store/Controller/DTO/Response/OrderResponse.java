package com.example.clothes_store.Controller.DTO.Response;

import com.example.clothes_store.Model.Eum.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long orderId;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private OrderStatus orderStatus;

    private Integer totalItems;

    private List<OrderItemResponse> items;
}
