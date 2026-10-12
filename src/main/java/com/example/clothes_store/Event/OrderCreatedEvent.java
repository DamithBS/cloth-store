package com.example.clothes_store.Event;

public record OrderCreatedEvent(
        Long orderId,
        Long userId
) {
}
