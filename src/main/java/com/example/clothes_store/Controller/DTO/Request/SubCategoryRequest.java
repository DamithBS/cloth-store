package com.example.clothes_store.Controller.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubCategoryRequest {

    @NotBlank(message = "Sub Category name is required")
    @Size(min = 2, max = 50, message = "Sub Category name must be between 2 and 50 characters")
    private String name;
}
