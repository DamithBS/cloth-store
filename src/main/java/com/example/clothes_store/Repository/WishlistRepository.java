package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {

    Optional<Wishlist> findByUserUserName(String userName);
}
