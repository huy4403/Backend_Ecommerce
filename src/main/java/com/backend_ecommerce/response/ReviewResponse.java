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
public class ReviewResponse {
    private Long id;
    private String user;
    private double rating;
    private String comment;
    private List<String> images;
    private ReplyResponse reply;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime date;

    public static ReviewResponse mapFromReview(Review review) {

        ReplyResponse reply = null;

        if (review.getReply() != null) {
            reply = ReplyResponse.builder()
                    .content(review.getReply().getContent())
                    .replyDate(review.getCreatedAt())
                    .build();
        }

        return ReviewResponse
                .builder()
                .id(review.getId())
                .user(review.getName())
                .rating(review.getRating())
                .comment(review.getReviewText())
                .date(review.getCreatedAt())
                .images(review.getReviewImages())
                .reply(reply)
                .build();
    }
}
