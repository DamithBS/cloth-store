package com.example.clothes_store.Controller.DTO.Request;

import com.example.clothes_store.Model.Eum.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    @NotNull(message = "payment method is required")
    private PaymentMethod paymentMethod;
}
