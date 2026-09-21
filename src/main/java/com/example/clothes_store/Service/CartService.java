package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.AddToCartRequest;
import com.example.clothes_store.Controller.DTO.Request.UpdateCartItemRequest;
import com.example.clothes_store.Controller.DTO.Response.CartResponse;

public interface CartService {

    void addToCart(String userName, AddToCartRequest addToCartRequest);

    CartResponse getCart(String userName);

    void updateCartItem(String userName, Long cartItemId, UpdateCartItemRequest updateCartItemRequest);

    void deleteCartItem(String userName, Long cartItemId);


}
