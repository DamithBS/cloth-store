package com.example.clothes_store.Controller.DTO.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {
    private Long reviewId;

    private Long productId;

    private String productName;

    private LocalDateTime createdAt;

    private String userName;

    private Integer rating;

    private String comment;
}
