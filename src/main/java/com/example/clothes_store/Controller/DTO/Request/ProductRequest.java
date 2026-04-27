package com.example.clothes_store.Controller.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal basePrice;

    @NotNull
    private Long subCategoryId;

    @NotNull
    private Long brandId;

    @NotEmpty(message = "At least one variant is required")
    private List<ProductVariantRequest> variants;

    @NotEmpty
    private List<String> images;
}
