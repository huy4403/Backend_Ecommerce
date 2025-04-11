package com.backend_ecommerce.request;

import com.backend_ecommerce.domain.ReviewStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewStatusRequest {
    private ReviewStatus status;
}
