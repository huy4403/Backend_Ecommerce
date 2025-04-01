package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.Review;
import com.backend_ecommerce.repository.OrderItemRepository;
import com.backend_ecommerce.repository.ProductRepository;
import com.backend_ecommerce.repository.ReviewRepository;
import com.backend_ecommerce.request.ReviewRequest;
import com.backend_ecommerce.response.ReviewResponse;
import com.backend_ecommerce.service.CloudinaryService;
import com.backend_ecommerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CloudinaryService uploadImage;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Long addReview(ReviewRequest req) {

        Product product = productRepository.findById(req.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        if(!orderItemRepository.existsByProductIdAndUserIdAndDelivered(product.getId(),
                userPrincipal.user().getId(),
                OrderStatus.DELIVERED))
            throw new ResourceNotFoundException("You do not permission to add this review");


        List<String> reviewImages = Optional.ofNullable(req.getReviewImages())
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(uploadImage::uploadImage)
                        .collect(Collectors.toList()))
                .orElse(null);


        Review review = Review
                .builder()
                .name(userPrincipal.user().getFullName())
                .rating(req.getRating())
                .reviewText(req.getComment())
                .reviewImages(reviewImages)
                .product(product)
                .user(userPrincipal.user())
                .build();

        return reviewRepository.save(review).getId();
    }

    @Override
    public List<ReviewResponse> getReviews(Long id, int page, int limit) {

        Pageable pageable = PageRequest.of(
                page - 1,
                limit,
                Sort.unsorted());

        Page<Review> result = reviewRepository.findAllByProductId(id, pageable);

        return result.getContent().stream().map(
                ReviewResponse::mapFromReview
        ).collect(Collectors.toList());
    }
}
