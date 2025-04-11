package com.backend_ecommerce.controller.review;

import com.backend_ecommerce.request.ReviewReplyRequest;
import com.backend_ecommerce.request.ReviewStatusRequest;
import com.backend_ecommerce.response.AdminReviewsResponse;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.ReviewResponse;
import com.backend_ecommerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/review")
@RequiredArgsConstructor
public class AdminReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getAllReviews(@RequestParam(name = "page", defaultValue = "1") Integer page) {
        List<AdminReviewsResponse> reviews = reviewService.getReviewsForAdmin(page);
        return ApiResponse.ok("All review", reviews);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<?> reply(@PathVariable("id") Long id, @RequestBody ReviewReplyRequest reply) {
        Long reviewReplyId = reviewService.AddReply(id, reply);
        return ApiResponse.ok("Reply added to the review", reviewReplyId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Long id, @RequestBody ReviewStatusRequest status) {
        Long reviewId = reviewService.changeStatus(id, status.getStatus());
        return ApiResponse.ok("Review status updated", reviewId);
    }
}
