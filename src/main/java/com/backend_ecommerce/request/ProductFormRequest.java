package com.backend_ecommerce.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductFormRequest {

    private String title;

    private Long categoryId;

    private Long importPrice;

    private Long price;

    private String description;

    private String brand;

    private List<MultipartFile> files = new ArrayList<>();

}
