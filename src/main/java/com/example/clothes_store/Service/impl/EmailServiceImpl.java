package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Model.Entity.Order;
import com.example.clothes_store.Model.Entity.OrderItem;
import com.example.clothes_store.Model.Entity.ProductVariant;
import com.example.clothes_store.Model.Entity.User;
import com.example.clothes_store.Service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    // Inject JavaMailSender to send emails using Spring Mail.
    private final JavaMailSender mailSender;


    @Override
    public void sendOrderConfirmationEmail(User user, Order order){

        // Validate that the user has a valid email address.
        if (user.getEmail() == null || user.getEmail().isBlank()){
            throw  new IllegalStateException("User email address is missing");
        }

        // Build the order confirmation email body.
        StringBuilder body = new StringBuilder();

        body.append("Hello ")
                .append(user.getUserName())
                .append(",\n\n");

        body.append("Your order has been created successfully!\n\n");

        body.append("Order ID: ")
                .append(order.getId())
                .append("\n\n");

        body.append("========== ORDER ITEMS ==========\n\n");

        // Iterate through all items included in the order.
        for (OrderItem item : order.getOrderItems()) {

            // Get the product details associated with the order item.
            ProductVariant variant = item.getProductVariant();

            String productName =
                    variant.getProduct().getName();

            int quantity = item.getQuantity();

            // Assumes item.getPrice() stores the unit price.
            BigDecimal unitPrice = item.getPrice();

            BigDecimal itemTotal = unitPrice.multiply(
                    BigDecimal.valueOf(quantity)
            );

            // Append the product details to the email body.
            body.append("Product: ")
                    .append(productName)
                    .append("\n");

            body.append("Quantity: ")
                    .append(quantity)
                    .append("\n");

            body.append("Unit Price: Rs. ")
                    .append(unitPrice.toPlainString())
                    .append("\n");

            body.append("Item Total: Rs. ")
                    .append(itemTotal.toPlainString())
                    .append("\n\n");
        }

        body.append("=================================\n");

        // Add a separator after the order item details.
        body.append("Order Total: Rs. ")
                .append(order.getTotalAmount().toPlainString())
                .append("\n");

        body.append("Order Status: ")
                .append(order.getOrderStatus())
                .append("\n\n");

        body.append("Thank you for shopping with us!");

        // Create a simple text-based email message.
        SimpleMailMessage message = new SimpleMailMessage();

        // Set the recipient's email address and order details.
        message.setTo(user.getEmail());
        message.setSubject( "Order Confirmation - #" + order.getId());

        // Set the email body using the content built above.
        message.setText(body.toString());

        // Send the email using Spring's JavaMailSender.
        mailSender.send(message);

        log.info(
                "Order confirmation email sent. orderId={}, userId={}",
                order.getId(),
                user.getId()
        );
    }


}
