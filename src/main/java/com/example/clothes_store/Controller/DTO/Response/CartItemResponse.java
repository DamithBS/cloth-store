package com.example.clothes_store.Controller.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CartItemResponse {

    private Long cartItemId;

    private Long productVariantId;

    private String productName;

    private String size;

    private String color;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subtotal;

    private Integer availableStock;

}
