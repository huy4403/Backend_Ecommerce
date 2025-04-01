package com.backend_ecommerce.response;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.model.Review;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeaturedProductsResponse {
    private Long id;
    private String title;
    private Long price;
    private String image;
    private double rating;
    private Long reviews;

    public static FeaturedProductsResponse mapFrom(Product product) {

        double averageRating = product.getReviews().stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        return FeaturedProductsResponse
                .builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .image(product.getImages().getFirst())
                .rating(averageRating)
                .reviews((long) product.getReviews().size())
                .build();
    }
}
