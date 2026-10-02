package com.example.clothes_store.Repository;

import com.example.clothes_store.Model.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<Review> findByProductId(Long productId);

    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);
}
