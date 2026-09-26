package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Response.OrderItemResponse;
import com.example.clothes_store.Controller.DTO.Response.OrderResponse;
import com.example.clothes_store.Exception.InsufficientStockException;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.*;
import com.example.clothes_store.Model.Eum.OrderStatus;
import com.example.clothes_store.Repository.*;
import com.example.clothes_store.Service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    //create order in all cart items
    @Override
    @Transactional
    public void createOrder(String userName){

        User user = getUser(userName);

        //  Find user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found")
                );

        // Check cart items
        if (cart.getCartItems()==null || cart.getCartItems().isEmpty()){
            throw new ResourceNotFoundException("Cannot place order. Cart is empty");
        }

        /*
         * Create a copy because the cart items will
         * be removed after the order is created.
         */
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());

        createOrder(cartItems,cart);



    }


    // create order in single item
    @Override
    @Transactional
    public void orderSingleItem(String userName,Long cartItemId){

        // find the Authenticated user
        User user = getUser(userName);

        //  Find user's cart
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found")
                );

        /*
         * This query also verifies that the cart item
         * belongs to the authenticated user's cart.
         */
        CartItem cartItem = cartItemRepository
                .findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found"
                        ));

        createOrder(
                List.of(cartItem),
                cart
        );

    }

    // get the all order details
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String userName) {

        // find the Authenticated user
        User user = getUser(userName);

        List <Order> orders = orderRepository.findAllByUserIdOrderByCreateAtDesc(user.getId());

        if (orders.isEmpty()){
            throw new ResourceNotFoundException("Order not found");
        }


        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();


    }

    // HELPER METHODS

    // find the Authenticated user
    private User getUser(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("Authenticated user not found"));
    }


     // COMMON ORDER CREATION
    private void createOrder(List<CartItem> cartItems,Cart cart){

        // Create Order
        Order order =new Order();

        order.setUser(cart.getUser());
        order.setCreateAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());

        BigDecimal totalAmount= BigDecimal.ZERO;


        // Process each CartItem

        for (CartItem cartItem : cartItems){

            //Validate quantity
            if (cartItem.getQuantity() == null || cartItem.getQuantity() < 1){
                throw new IllegalArgumentException("Cart item quantity must be at least 1");
            }

            //Get ProductVariant
            ProductVariant productVariant = cartItem.getProductVariant();

            if (productVariant == null){
                throw new ResourceNotFoundException( "Product variant not found for cart item ");
            }

            /*
             * Lock inventory row.
             *
             * PESSIMISTIC_WRITE prevents two users
             * from modifying the same stock simultaneously.
             */
            Inventory inventory = inventoryRepository.findByProductVariantIdForUpdate(productVariant.getId())
                    .orElseThrow( ()-> new ResourceNotFoundException("Inventory not found for product variant ")
                    );


            if (inventory.getStockQuantity() == null){
                throw new IllegalStateException("Inventory stock quantity cannot be null");
            }

            //Check available stock
            if (cartItem.getQuantity() > inventory.getStockQuantity()){
                throw  new InsufficientStockException(
                        "Only "
                                + inventory.getStockQuantity()
                                + " items are available for "
                                + productVariant
                                .getProduct()
                                .getName()
                );

            }

            // Get current product price
            BigDecimal price = productVariant.getPrice();

            if (price == null){
                throw new IllegalStateException( "Product variant price cannot be null");
            }

            //Calculate subtotal
            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())
            );


            //Create OrderItem
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProductVariant(productVariant);
            orderItem.setPrice(price);
            orderItem.setQuantity(cartItem.getQuantity());

            //Add OrderItem to Order
            order.getOrderItems().add(orderItem);

            //Calculate order total
            totalAmount = totalAmount.add(subtotal);

            //Reduce inventory
            inventory.setStockQuantity(
                inventory.getStockQuantity() - cartItem.getQuantity()
            );
        }

        // SAVE ORDER

        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        // REMOVE PURCHASED ITEMS FROM CART
        for (CartItem cartItem : cartItems){
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }


    }

    // MAP ORDER ITEM -> RESPONSE
    private OrderItemResponse mapToOrderItemResponse(
            OrderItem orderItem
    ){
        ProductVariant variant = orderItem.getProductVariant();
        BigDecimal subtotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        return OrderItemResponse.builder()
                .orderItemId(orderItem.getId())
                .productVariantId(variant.getId())
                .productName(variant.getProduct().getName())
                .size(variant.getSize())
                .color(variant.getColor())
                .sku(variant.getSku())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .subtotal(subtotal)
                .build();
    }



    // MAP ORDER -> RESPONSE
    private OrderResponse mapToOrderResponse(
            Order order
    ){
        int totalItems = order
                .getOrderItems()
                .stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        List<OrderItemResponse> itemResponses = order.getOrderItems()
                .stream()
                .map(this::mapToOrderItemResponse)
                .toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreateAt())
                .orderStatus(order.getOrderStatus())
                .totalItems(totalItems)
                .items(itemResponses)
                .build();
    }
}
