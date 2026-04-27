package com.example.clothes_store.Controller.DTO.Response;

import com.example.clothes_store.Model.Entity.ProductImage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {

    private String name;
    private String description;
    private BigDecimal basePrice;
    private String categoryName;
    private String subCategoryName;
    private String brandName;

    private List<ProductVariantResponse> variants;

    private List<String> images;
}
