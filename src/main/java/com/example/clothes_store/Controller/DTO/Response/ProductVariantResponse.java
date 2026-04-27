package com.example.clothes_store.Controller.DTO.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantResponse {

    private String size;
    private String color;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
}
