package com.example.clothes_store.Event;

import java.math.BigDecimal;

public record ProductCreatedEvent(
        Long productId,
        String productName,
        BigDecimal basePrice
) {
}
