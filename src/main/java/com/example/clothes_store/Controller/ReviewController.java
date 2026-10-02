package com.example.clothes_store.Controller;

import com.example.clothes_store.Controller.DTO.Request.ReviewRequest;
import com.example.clothes_store.Controller.DTO.Response.ReviewResponse;
import com.example.clothes_store.Service.ReviewService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @RolesAllowed("USER")
    @PostMapping("product/{productId}")
    public ResponseEntity<String> createReview(
            Authentication authentication,
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequest reviewRequest
            ){

        String userName = authentication.getName();
        reviewService.createReview(userName,reviewRequest,productId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Review added successfully");
    }


    @GetMapping("product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReview(
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }


    @RolesAllowed("USER")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication
    ){
        String userName =authentication.getName();
        reviewService.deleteReview(reviewId,userName);

        return ResponseEntity.ok("Review deleted successfully ");
    }
}
