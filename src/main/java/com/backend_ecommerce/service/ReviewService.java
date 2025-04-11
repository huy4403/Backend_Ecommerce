package com.backend_ecommerce.service;

import com.backend_ecommerce.domain.ReviewStatus;
import com.backend_ecommerce.request.ReviewReplyRequest;
import com.backend_ecommerce.request.ReviewRequest;
import com.backend_ecommerce.response.AdminReviewsResponse;
import com.backend_ecommerce.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    Long addReview(ReviewRequest req);

    List<ReviewResponse> getReviews(Long id, int page, int limit);

    Long AddReply(Long id, ReviewReplyRequest reply);

    List<AdminReviewsResponse> getReviewsForAdmin(Integer page);

    Long changeStatus(Long id, ReviewStatus status);
}
