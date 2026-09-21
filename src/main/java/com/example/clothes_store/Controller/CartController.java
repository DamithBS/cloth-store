package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.AddToCartRequest;
import com.example.clothes_store.Controller.DTO.Request.UpdateCartItemRequest;
import com.example.clothes_store.Controller.DTO.Response.CartResponse;
import com.example.clothes_store.Service.CartService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @RolesAllowed("USER")
    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(
            @Valid @RequestBody AddToCartRequest addToCartRequest,
            Authentication authentication
            ){
            String userName= authentication.getName();
            cartService.addToCart(userName,addToCartRequest);

            return ResponseEntity.ok("Product added to cart successfully");
    }


    @RolesAllowed("USER")
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
           Authentication authentication
    ){
        String userName= authentication.getName();
        CartResponse cartResponse = cartService.getCart(userName);

        return ResponseEntity.ok(cartResponse);
    }

    @RolesAllowed("USER")
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<String> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest,
            Authentication authentication
    ){
        String userName= authentication.getName();
        cartService.updateCartItem(userName,cartItemId,updateCartItemRequest);

        return ResponseEntity.ok("Cart item updated successfully");
    }

    @RolesAllowed("USER")
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(
            @PathVariable Long cartItemId,
           Authentication authentication
    ){
        String userName= authentication.getName();
        cartService.deleteCartItem(userName,cartItemId);

        return ResponseEntity.ok("Cart item removed successfully");
    }



}
