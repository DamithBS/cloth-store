package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Response.DeliveryResponse;
import com.example.clothes_store.Service.DeliveryService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @RolesAllowed("USER")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryResponse> getDelivery(
            Authentication authentication,
            @PathVariable Long orderId
    ){

        String userName = authentication.getName();
        DeliveryResponse deliveryResponse = deliveryService.getDeliveryByOrderId(orderId,userName);

        return ResponseEntity.ok(deliveryResponse);
    }


    @RolesAllowed("ADMIN")
    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<String> shipDelivery(
            @PathVariable Long orderId
    ){
        deliveryService.shipDelivery(orderId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Delivery marked as SHIPPED successfully");
    }


    @RolesAllowed("ADMIN")
    @PatchMapping("{orderId}/deliver")
    public ResponseEntity<String> deliverOrder(
            @PathVariable Long orderId
    ){
        deliveryService.markAsDelivered(orderId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Delivery marked as DELIVERED successfully");
    }


    @RolesAllowed("ADMIN")
    @PatchMapping("{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId
    ){
        deliveryService.cancelDelivery(orderId);

        return ResponseEntity.ok("Delivery cancelled successfully");
    }
}


