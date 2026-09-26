package com.example.clothes_store.Controller.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {

    private Long orderItemId;

    private Long productVariantId;

    private String productName;

    private String size;

    private String color;

    private String sku;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subtotal;
}
