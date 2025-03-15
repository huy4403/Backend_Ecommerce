package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Review;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ReviewResponse {
    private Long id;
    private String name;
    private double rating;
    private String reviewText;

    public static ReviewResponse mapFromReview(Review review) {

        return ReviewResponse
                .builder()
                .id(review.getId())
                .name(review.getName())
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .build();
    }
}
