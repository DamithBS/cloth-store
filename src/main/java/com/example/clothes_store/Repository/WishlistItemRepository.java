package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem,Long> {

    Optional<WishlistItem> findByIdAndWishlistId(Long itemId, Long wishlistId);

    boolean existsByWishlistIdAndProductId( Long wishlistId, Long productId);


}
