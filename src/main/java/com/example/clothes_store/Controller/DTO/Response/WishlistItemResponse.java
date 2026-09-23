package com.example.clothes_store.Controller.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WishlistItemResponse {

    private Long wishlistItemId;

    private Long productId;

    private String productName;

    private String description;

    private BigDecimal basePrice;



}
