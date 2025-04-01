package com.backend_ecommerce.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    private Long productId;

    private double rating;

    private String comment;

    private List<MultipartFile> reviewImages;

}
