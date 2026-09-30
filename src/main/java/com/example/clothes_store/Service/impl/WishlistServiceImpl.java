package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Response.WishlistItemResponse;
import com.example.clothes_store.Controller.DTO.Response.WishlistResponse;
import com.example.clothes_store.Exception.InsufficientStockException;
import com.example.clothes_store.Exception.ProductAlreadyInWishlistException;
import com.example.clothes_store.Exception.ProductNotFoundException;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.*;
import com.example.clothes_store.Repository.*;
import com.example.clothes_store.Service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final InventoryRepository inventoryRepository;


    // ADD PRODUCT TO WISHLIST
    @Override
    @Transactional
    public void addToWishlist(String userName, Long productVariantId){

        // Find authenticated user
        User user = getUser(userName);

        // Find product
        ProductVariant productVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(()-> new ProductNotFoundException("Product Variant not found "));

        // create wish list find user
        Wishlist wishlist = wishlistRepository.findByUserUserName(userName)
                .orElseGet(() -> createWishlist(user));

        //  Check duplicate
        boolean alreadyExists = wishlistItemRepository.existsByWishlistIdAndProductVariantId(
                wishlist.getId(),
                productVariantId
        );
        if (alreadyExists){
            throw new ProductAlreadyInWishlistException("Product Variant is already in your wishlist");
        }

        // Create WishlistItem
        WishlistItem wishlistItem= new WishlistItem();

        wishlistItem.setProductVariant(productVariant);
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



    // wish list items add to the cart
    @Override
    @Transactional
    public void addWishlistItemToCart(String userName,Long wishlistItemId){

        // Find authenticated user
        User user = getUser(userName);


        // find wish list in username
        Wishlist wishlist = wishlistRepository.findByUserUserName(userName)
                .orElseThrow(()-> new ResourceNotFoundException("Wishlist not found")
                );

        // Find item belonging to this wishlist
        WishlistItem wishlistItem= wishlistItemRepository.findByIdAndWishlistId(wishlistItemId,wishlist.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Wishlist item not found ")
                );

        // Get ProductVariant
        ProductVariant productVariant = wishlistItem.getProductVariant();

        //Find inventory
        Inventory inventory = inventoryRepository.findByProductVariantId(productVariant.getId())
                .orElseThrow(()-> new ResourceNotFoundException( "Inventory not found"));


        //Check stock
        if (inventory.getStockQuantity() == null || inventory.getStockQuantity() < 1){
            throw new InsufficientStockException("Product is out of stock");
        }

        //Find user's cart or create one
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(()-> {
                    Cart newCart =new Cart();

                    newCart.setUser(user);
                    newCart.setCreated_at(LocalDateTime.now());

                    return cartRepository.save(newCart);
                });

        //Check whether this variant already exists in cart
        CartItem cartItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(),productVariant.getId())
                .orElse(null);

        //If already exists → increase quantity
        if (cartItem != null){
            int newQuantity = cartItem.getQuantity() + 1;

            if (newQuantity > inventory.getStockQuantity()){
                throw new InsufficientStockException("items are not available");

            }

            cartItem.setQuantity(newQuantity);

        }

        // Otherwise create new CartItem
        else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariant(productVariant);
            cartItem.setQuantity(1);
        }

        // Save CartItem
        cartItemRepository.save(cartItem);
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
         ProductVariant productVariant = wishlistItem.getProductVariant();
        Product product =productVariant.getProduct();

        return WishlistItemResponse.builder()
                .wishlistItemId(wishlistItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .description(product.getDescription())
                .basePrice(product.getBasePrice())
                .color(productVariant.getColor())
                .sku(productVariant.getSku())
                .size(productVariant.getSize())
                .build();
    }
}
