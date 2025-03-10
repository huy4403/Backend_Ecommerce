package com.backend_ecommerce.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProductRequest {

    private String title;

    private Long categoryId;

    private Long price;

    private String description;

    private List<MultipartFile> files = new ArrayList<>();
}
