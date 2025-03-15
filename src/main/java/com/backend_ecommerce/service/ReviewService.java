package com.backend_ecommerce.service;

import com.backend_ecommerce.request.ReviewRequest;
import com.backend_ecommerce.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    Long addReview(ReviewRequest req);

    List<ReviewResponse> getReviews(Long id, int page, int limit);
}
