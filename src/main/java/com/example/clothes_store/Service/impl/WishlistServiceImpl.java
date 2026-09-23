package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Response.WishlistItemResponse;
import com.example.clothes_store.Controller.DTO.Response.WishlistResponse;
import com.example.clothes_store.Exception.ProductAlreadyInWishlistException;
import com.example.clothes_store.Exception.ProductNotFoundException;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.Product;
import com.example.clothes_store.Model.Entity.User;
import com.example.clothes_store.Model.Entity.Wishlist;
import com.example.clothes_store.Model.Entity.WishlistItem;
import com.example.clothes_store.Repository.ProductRepository;
import com.example.clothes_store.Repository.UserRepository;
import com.example.clothes_store.Repository.WishlistItemRepository;
import com.example.clothes_store.Repository.WishlistRepository;
import com.example.clothes_store.Service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    // ADD PRODUCT TO WISHLIST
    @Override
    @Transactional
    public void addToWishlist(String userName, Long productId){

        // Find authenticated user
        User user = getUser(userName);

        // Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("Product not found "));

        // create wish list find user
        Wishlist wishlist = wishlistRepository.findByUserUserName(userName)
                .orElseGet(() -> createWishlist(user));

        //  Check duplicate
        boolean alreadyExists = wishlistItemRepository.existsByWishlistIdAndProductId(
                wishlist.getId(),
                productId
        );
        if (alreadyExists){
            throw new ProductAlreadyInWishlistException("Product is already in your wishlist");
        }

        // Create WishlistItem
        WishlistItem wishlistItem= new WishlistItem();

        wishlistItem.setProduct(product);
        wishlistItem.setWishlist(wishlist);

        // Add item to wishlist
        wishlist.getWishlistItems().add(wishlistItem);

        wishlistRepository.save(wishlist);

    }


    // GET WISHLIST
    @Override
    @Transactional(readOnly = true)
    public WishlistResponse getWishlist(String userName){


        // find wish list in username
        Wishlist wishlist = wishlistRepository.findByUserUserName(userName)
                .orElseThrow(()-> new ResourceNotFoundException("Wishlist not found")
                );

        //Get items and Convert to Response
        List<WishlistItemResponse> itemResponses = wishlist.getWishlistItems()
                .stream()
                .map(this::mapToWishlistItemResponse)
                .toList();

        //Return wishlist response
        return WishlistResponse.builder()
                .wishlistId(wishlist.getId())
                .items(itemResponses)
                .totalItems(itemResponses.size())
                .build();

    }


    //delete wishlist item
    @Override
    @Transactional
    public void deleteWishlistItem(String userName, Long wishlistItemId){

        // Find authenticated user
        User user =getUser(userName);

        // find wish list in username
        Wishlist wishlist = wishlistRepository.findByUserUserName(userName)
                .orElseThrow(()-> new ResourceNotFoundException("Wishlist not found")
                );

        // Find item belonging to this wishlist
        WishlistItem wishlistItem= wishlistItemRepository.findByIdAndWishlistId(wishlistItemId,wishlist.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Wishlist item not found ")
                );

        // remove items
        wishlist.getWishlistItems().remove(wishlistItem);
        wishlistRepository.save(wishlist);

    }


     //---------------------------------
    // HELPER METHODS


    // Find authenticated user
    private User getUser(String userName){

        return userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with username")
                );
    }


    // create wish list
    private Wishlist createWishlist(User user){
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        return wishlistRepository.save(wishlist);
    }


    // wish list response data
    private WishlistItemResponse mapToWishlistItemResponse(
            WishlistItem wishlistItem
    ){
        Product product =wishlistItem.getProduct();

        return WishlistItemResponse.builder()
                .wishlistItemId(wishlistItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .description(product.getDescription())
                .basePrice(product.getBasePrice())
                .build();
    }
}
