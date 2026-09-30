package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.PaymentRequest;
import com.example.clothes_store.Controller.DTO.Response.PaymentResponse;
import com.example.clothes_store.Model.Eum.PaymentMethod;
import com.example.clothes_store.Service.PaymentService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @RolesAllowed("USER")
    @PostMapping("/{orderId}")
    public ResponseEntity<String> makePayment(
            Authentication authentication,
            @PathVariable Long orderId,
            @RequestBody PaymentRequest paymentRequest
            ){
        String userName =authentication.getName();

        paymentService.makePayment(userName,orderId,paymentRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("payment ok");
    }


    @RolesAllowed("USER")
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPayment(
            Authentication authentication,
            @PathVariable Long orderId
    ){
        String userName = authentication.getName();
        PaymentResponse paymentResponse = paymentService.getPayment(userName,orderId);
        return ResponseEntity.ok(paymentResponse);

    }
}
