package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.AddToCartRequest;
import com.example.clothes_store.Controller.DTO.Request.UpdateCartItemRequest;
import com.example.clothes_store.Controller.DTO.Response.CartItemResponse;
import com.example.clothes_store.Controller.DTO.Response.CartResponse;
import com.example.clothes_store.Exception.InsufficientStockException;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.*;
import com.example.clothes_store.Repository.*;
import com.example.clothes_store.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryRepository inventoryRepository;


    @Override
    @Transactional
    public void addToCart(String userName, AddToCartRequest addToCartRequest){

        // Find authenticated user
        User user = getUser(userName);


        // Find user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();

                    newCart.setUser(user);
                    newCart.setCreated_at(LocalDateTime.now());

                    return cartRepository.save(newCart);
                });

        // Find ProductVariant
        ProductVariant productVariant= productVariantRepository.findById(addToCartRequest.getProductVariantId())
                .orElseThrow(()-> new ResourceNotFoundException("Product variant not found"));

        //Find Inventory
        Inventory inventory= inventoryRepository.findByProductVariantId(productVariant.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Inventory not found for this product variant"));


        //Find existing CartItem
        CartItem cartItem = cartItemRepository.findByCartIdAndProductVariantId(
                cart.getId(),
                productVariant.getId()
        ).orElse(null);

        //Calculate final quantity
        int finalQuantity;

        if (cartItem != null){
            finalQuantity= cartItem.getQuantity()+addToCartRequest.getQuantity();
        }
        else {
            finalQuantity= addToCartRequest.getQuantity();
        }


        // Check stock

        if (finalQuantity < 1) {
            throw new InsufficientStockException(
                    "Quantity must be at least 1"
            );
        }

        if (finalQuantity > inventory.getStockQuantity()){
            throw new InsufficientStockException(   "Only "
                    + inventory.getStockQuantity()
                    + " items are available");
        }

        //Update existing CartItem

        if (cartItem != null){
            cartItem.setQuantity(finalQuantity);
        }

        // Create new CartItem
        else {
            cartItem =new CartItem();
            cartItem.setQuantity(addToCartRequest.getQuantity());
            cartItem.setCart(cart);
            cartItem.setProductVariant(productVariant);


        }

        // Save CartItem
        cartItemRepository.save(cartItem);

    }


    //get cart details
    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String userName){

        // Find authenticated user
        User user = getUser(userName);

        //  Find user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cart not found"
                        )
                );


        // 3. Convert CartItems -> CartItemResponse

        List<CartItemResponse> itemResponses= cart.getCartItems()
                .stream()
                .map(cartItem -> {
                    ProductVariant variant = cartItem.getProductVariant();
                    BigDecimal price = variant.getPrice();
                    BigDecimal subtotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity())
                    );

                    return CartItemResponse.builder()
                            .cartItemId(cartItem.getId())
                            .productVariantId(variant.getId())
                            .productName(variant.getProduct().getName())
                            .size(variant.getSize())
                            .color(variant.getColor())
                            .price(price)
                            .quantity(cartItem.getQuantity())
                            .subtotal(subtotal)
                            .availableStock(variant.getInventory().getStockQuantity()
                            )
                            .build();

                })
                .toList();


        // Calculate total
        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );


        //  Return response

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .total(total)
                .build();

    }


    // update cart items
    @Override
    @Transactional
    public void updateCartItem(String userName, Long cartItemId, UpdateCartItemRequest updateCartItemRequest){

        //  Find authenticated user
        User user =getUser(userName);


        //  Find user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cart not found"
                        )
                );


        //  Find CartItem
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException( "Cart item not found"));


        // Make sure CartItem belongs to this user's cart
        if (!cartItem.getCart().getId().equals(cart.getId())){
            throw new ResourceNotFoundException("Cart item not found");
        }

        // Get ProductVariant
        ProductVariant productVariant= cartItem.getProductVariant();

        //  Get Inventory
        Inventory inventory= inventoryRepository.findByProductVariantId(productVariant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        // Check stock
        if (updateCartItemRequest.getQuantity() > inventory.getStockQuantity()){
            throw new InsufficientStockException("only"+inventory.getStockQuantity()+"items are available");
        }

        // Update quantity
        cartItem.setQuantity(updateCartItemRequest.getQuantity());

        cartItemRepository.save(cartItem);

    }


    //delete cart item
    @Override
    @Transactional
    public void deleteCartItem(String userName, Long cartItemId){
        // Find authenticated user
        User user = getUser(userName);

        // Find user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cart not found"
                        )
                );


        // Find CartItem
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Cart item not found"
                        )
                );

        //  Make sure this CartItem belongs to
        //    the authenticated user's cart
        if (! cartItem.getCart().getId().equals(cart.getId())){
            throw  new ResourceNotFoundException("Cart item not found");
        }

        // Remove CartItem from Cart
        cart.getCartItems().remove(cartItem);


        // Delete CartItem
        cartItemRepository.delete(cartItem);

    }




    // HELPER METHODS

    // find the Authenticated user
    private  User getUser(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("Authenticated user not found"));
    }

}
