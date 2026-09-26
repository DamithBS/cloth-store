package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByCartIdAndProductVariantId(
            Long cartId,
            Long productVariantId
    );

    Optional<CartItem> findByIdAndCartId(
            Long cartItemId,
            Long cartId
    );


}
