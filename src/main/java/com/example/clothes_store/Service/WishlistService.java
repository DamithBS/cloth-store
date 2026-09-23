package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Response.WishlistResponse;

public interface WishlistService {

    void addToWishlist(String userName, Long productId);

    WishlistResponse getWishlist(String userName);

    void deleteWishlistItem(String userName, Long wishlistItemId);
}
