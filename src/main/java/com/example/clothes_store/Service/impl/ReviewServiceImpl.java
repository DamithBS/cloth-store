package com.example.clothes_store.Service.impl;

import com.example.clothes_store.Controller.DTO.Request.ReviewRequest;
import com.example.clothes_store.Controller.DTO.Response.ReviewResponse;
import com.example.clothes_store.Exception.ProductNotFoundException;
import com.example.clothes_store.Exception.ResourceNotFoundException;
import com.example.clothes_store.Model.Entity.Product;
import com.example.clothes_store.Model.Entity.Review;
import com.example.clothes_store.Model.Entity.User;
import com.example.clothes_store.Repository.OrderRepository;
import com.example.clothes_store.Repository.ProductRepository;
import com.example.clothes_store.Repository.ReviewRepository;
import com.example.clothes_store.Repository.UserRepository;
import com.example.clothes_store.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    // CREATE REVIEW
    @Override
    @Transactional
    public void createReview(String userName, ReviewRequest reviewRequest , Long productId){

        // Find authenticated user
        User user =getUser(userName);

        // Find product
        Product product = findProduct(productId);

        // Check whether user already reviewed this product
        if (reviewRepository.existsByUserIdAndProductId(user.getId(),productId)){
            throw new IllegalStateException("You have already reviewed this product");
        }


        // Check whether user purchased this product
        // and the product was delivered
        boolean hasCompletedOrder =orderRepository.existsDeliveredOrderContainingProduct(
                user.getId(),
                product.getId()
        );

        if (!hasCompletedOrder){
            throw new IllegalStateException("You can review this product only after completing an order");
        }

        // Create Review and save
        Review review = new Review();

        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);
        review.setProduct(product);

        reviewRepository.save(review);

    }



    // GET REVIEWS BY PRODUCT
    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse>getReviewsByProduct(Long productId){

        // Find product
        findProduct(productId);

        // Get reviews
        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::mapToReviewResponse)
                .toList();
    }



    // DELETE REVIEW
    @Override
    @Transactional
    public void deleteReview(Long reviewId, String userName){

        // Find authenticated user
        User user =getUser(userName);

        // Find review
        Review review = reviewRepository.findByIdAndUserId(reviewId, user.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Review not found")
                );

        reviewRepository.delete(review);

    }


    //--------------------------------------
    // HELPER METHODS

    // Find authenticated user
    private User getUser(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()-> new UsernameNotFoundException("Authenticated user not found")
                );
    }

    // Find product
    private Product findProduct(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("product not found")
                );
    }



    // ENTITY -> DTO MAPPING
    private ReviewResponse mapToReviewResponse(Review review){

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getName())
                .userName(review.getUser().getUserName())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();


    }
}
