package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AdminReviewsResponse {
    private Long id;
    private String user;
    private Long productId;
    private String product;
    private double rating;
    private String comment;
    private List<String> images;
    private ReplyResponse reply;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime date;

    public static AdminReviewsResponse mapFromReview(Review review) {

        ReplyResponse reply = null;

        if (review.getReply() != null) {
            reply = ReplyResponse.builder()
                    .content(review.getReply().getContent())
                    .replyDate(review.getCreatedAt())
                    .build();
        }

        return AdminReviewsResponse
                .builder()
                .id(review.getId())
                .user(review.getName())
                .productId(review.getProduct().getId())
                .product(review.getProduct().getTitle())
                .rating(review.getRating())
                .comment(review.getReviewText())
                .date(review.getCreatedAt())
                .images(review.getReviewImages())
                .reply(reply)
                .build();
    }
}
