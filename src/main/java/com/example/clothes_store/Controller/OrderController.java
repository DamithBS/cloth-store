package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Response.OrderResponse;
import com.example.clothes_store.Service.OrderService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @RolesAllowed("USER")
    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutAll(
            Authentication authentication
    ){
        String userName = authentication.getName();

        orderService.createOrder(userName);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("order create successfully ");

    }

    @RolesAllowed("USER")
    @PostMapping("/checkout/{cartItemId}")
    public ResponseEntity<String> checkoutSingleItem(
            Authentication authentication,
            @PathVariable Long cartItemId
    ){
        String userName = authentication.getName();
        orderService.orderSingleItem(userName,cartItemId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("order create successfully ");
    }


    @RolesAllowed("USER")
    @GetMapping("/details")
    public  ResponseEntity<List<OrderResponse>> getOrders(
            Authentication authentication
    ){
        String userName= authentication.getName();
       List <OrderResponse> orderResponse = orderService.getOrders(userName);

        return ResponseEntity.ok(orderResponse);
    }
}
