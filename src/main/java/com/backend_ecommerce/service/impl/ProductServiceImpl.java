package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.exception.ProductException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Category;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.repository.CategoryRepository;
import com.backend_ecommerce.repository.ProductRepository;
import com.backend_ecommerce.request.CreateProductRequest;
import com.backend_ecommerce.request.ProductHomeRequest;
import com.backend_ecommerce.request.UpdateProductRequest;
import com.backend_ecommerce.response.CreateProductResponse;
import com.backend_ecommerce.response.ProductDisplayResponse;
import com.backend_ecommerce.response.ProductElementResponse;
import com.backend_ecommerce.response.ProductHomeResponse;
import com.backend_ecommerce.service.CloudinaryService;
import com.backend_ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final CloudinaryService cloudinaryService;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest req) {

        Product prepareProduct = new Product();
        prepareProduct.setTitle(req.getTitle());
        prepareProduct.setPrice(req.getPrice());
        prepareProduct.setDescription(req.getDescription());

        Category categoryForProduct = categoryRepository.findById(req.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + req.getCategoryId())
        );

        prepareProduct.setCategory(categoryForProduct);

        if (!req.getFiles().isEmpty()) {

            List<String> images = new ArrayList<>();

            for (MultipartFile file : req.getFiles()) {
                String source = cloudinaryService.uploadImage(file);
                images.add(source);
            }
            prepareProduct.setImages(images);
        } else {
            prepareProduct.setImages(null);
        }
        return new CreateProductResponse(productRepository.save(prepareProduct).getId());
    }

    @Override
    public ProductHomeResponse getAllProduct(ProductHomeRequest req) {
        Pageable pageable = PageRequest.of(
                req.getPage(),
                req.getLimit(),
                Sort.unsorted());
        Page<Product> result = productRepository.filterProduct(req.getTitle(),
                req.getCategoryId(),
                req.getMinPrice(),
                req.getMaxPrice(),
                ProductStatus.ACTIVE,
                pageable
        );

        return ProductHomeResponse
                .builder()
                .products(result.getContent().stream().map(ProductElementResponse::mapFrom).collect(Collectors.toList()))
                .count(result.getTotalElements())
                .page(result.getTotalPages())
                .build();
    }

    @Override
    public ProductDisplayResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id)
        );
        if(product.getStatus() == ProductStatus.INACTIVE)
            throw new ProductException("Product is inactive");
        return ProductDisplayResponse.mapFromProduct(product);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id)
        );
        try {
            product.setStatus(ProductStatus.INACTIVE);
            productRepository.save(product);
        } catch (Exception e) {
            throw new ProductException("Something went wrong while deleting product");
        }
    }

    @Override
    public Long updateProduct(Long id, UpdateProductRequest req) {

        Product existProduct = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id)
        );
        Category categoryForProduct = categoryRepository.findById(req.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + req.getCategoryId())
        );

        existProduct.setTitle(req.getTitle());
        existProduct.setPrice(req.getPrice());
        existProduct.setCategory(categoryForProduct);
        existProduct.setDescription(req.getDescription());

        return productRepository.save(existProduct).getId();
    }

    @Override
    public Long openActiveProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id)
        );
        try {
            product.setStatus(ProductStatus.ACTIVE);
            return productRepository.save(product).getId();
        } catch (Exception e) {
            throw new ProductException("Something went wrong while open active product");
        }
    }
}
