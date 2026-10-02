package com.example.clothes_store.Service;

import com.example.clothes_store.Controller.DTO.Request.ReviewRequest;
import com.example.clothes_store.Controller.DTO.Response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    void createReview(String userName, ReviewRequest reviewRequest,Long productId);

    List<ReviewResponse> getReviewsByProduct(Long productId);

    void deleteReview(Long reviewId, String userName);
}
