package com.backend_ecommerce.controller.review;

import com.backend_ecommerce.request.ReviewRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> postReview(@ModelAttribute ReviewRequest req) {
        Long reviewId = reviewService.addReview(req);
        return ApiResponse.created("Add review successfully", reviewId);
    }
}
