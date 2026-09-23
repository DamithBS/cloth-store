package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Response.WishlistResponse;
import com.example.clothes_store.Service.WishlistService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @RolesAllowed("USER")
    @PostMapping("/items/{productId}")
    public ResponseEntity<String> addToWishlist(
            Authentication authentication,
            @PathVariable Long productId
    ){
        String userName= authentication.getName();
        wishlistService.addToWishlist(userName,productId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Product added to wishlist successfully");
    }


    @RolesAllowed("USER")
    @GetMapping("/items")
    public ResponseEntity<WishlistResponse> getWishlist(
            Authentication authentication
    ){
        String userName= authentication.getName();
        WishlistResponse wishlistResponse = wishlistService.getWishlist(userName);

        return ResponseEntity.ok(wishlistResponse);
    }


    @RolesAllowed("USER")
    @DeleteMapping("/items/{wishlistItemId}")
    public ResponseEntity<String> removeWishlistItem(
            Authentication authentication,
            @PathVariable Long wishlistItemId
    ){
        String userName= authentication.getName();
        wishlistService.deleteWishlistItem(userName,wishlistItemId);

        return ResponseEntity.ok("Delete wishlist item successfully");
    }
}
