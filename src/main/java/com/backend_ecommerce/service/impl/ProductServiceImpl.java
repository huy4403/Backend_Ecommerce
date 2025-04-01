package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.OrderStatus;
import com.backend_ecommerce.domain.ProductStatus;
import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.ProductException;
import com.backend_ecommerce.exception.ResourceNotFoundException;
import com.backend_ecommerce.model.Category;
import com.backend_ecommerce.model.Product;
import com.backend_ecommerce.repository.CategoryRepository;
import com.backend_ecommerce.repository.OrderItemRepository;
import com.backend_ecommerce.repository.ProductRepository;
import com.backend_ecommerce.request.ProductHomeRequest;
import com.backend_ecommerce.request.ProductManagementRequest;
import com.backend_ecommerce.request.ProductFormRequest;
import com.backend_ecommerce.response.*;
import com.backend_ecommerce.service.CloudinaryService;
import com.backend_ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final OrderItemRepository orderItemRepository;

    @Override
    public CreateProductResponse createProduct(ProductFormRequest req) {

        Product prepareProduct = new Product();
        prepareProduct.setTitle(req.getTitle());
        prepareProduct.setImportPrice(req.getImportPrice());
        prepareProduct.setPrice(req.getPrice());
        prepareProduct.setDescription(req.getDescription());
        prepareProduct.setBrand(req.getBrand());
        prepareProduct.setStatus(ProductStatus.INACTIVE);

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
                req.getPage() - 1,
                req.getLimit(),
                Sort.unsorted());
        Page<Product> result = productRepository.filterProduct(req.getTitle(),
                req.getCategoryName(),
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
        ProductDisplayResponse productDisplayResponse = ProductDisplayResponse.mapFromProduct(product);

        UserPrincipal userPrincipal;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userPrincipal = (UserPrincipal) authentication.getPrincipal();
            if(orderItemRepository.existsByProductIdAndUserIdAndDelivered(product.getId(),
                    userPrincipal.user().getId(),
                    OrderStatus.DELIVERED))
                productDisplayResponse.setBought(true);
        } catch (Exception e) {
            productDisplayResponse.setBought(false);
        }

        return productDisplayResponse;
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
    public Long updateProduct(Long id, ProductFormRequest req) {

        Product existProduct = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id)
        );
        Category categoryForProduct = categoryRepository.findById(req.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with id: " + req.getCategoryId())
        );

        existProduct.setTitle(req.getTitle());
        existProduct.setImportPrice(req.getImportPrice());
        existProduct.setPrice(req.getPrice());
        existProduct.setCategory(categoryForProduct);
        existProduct.setDescription(req.getDescription());
        existProduct.setBrand(req.getBrand());

        if (!req.getFiles().isEmpty()) {

            List<String> images = existProduct.getImages();

            for (MultipartFile file : req.getFiles()) {
                String source = cloudinaryService.uploadImage(file);
                images.add(source);
            }
            existProduct.setImages(images);
        }
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

    @Override
    public ProductManagementResponse getProductManagement(ProductManagementRequest req) {
        Pageable pageable = PageRequest.of(
                req.getPage() - 1,
                req.getLimit(),
                Sort.unsorted());
        Page<Product> result = productRepository.findProductManagement(req.getTitle(),
                req.getCategory(),
                req.getMinPrice(),
                req.getMaxPrice(),
                pageable
        );

        return ProductManagementResponse
                .builder()
                .products(result.getContent().stream().map(ProductForManagementResponse::mapFrom).collect(Collectors.toList()))
                .count(result.getTotalElements())
                .page(result.getTotalPages())
                .build();
    }

    @Override
    public ProductFillFormResponse findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id: " + id)
        );
        return ProductFillFormResponse.mapFrom(product);
    }

    @Override
    public List<ProductNewResponse> getNewProduct() {
        List<Product> products = productRepository.findTop4ByOrderByCreatedAtDesc();
        return products.stream().map(ProductNewResponse::mapFrom).collect(Collectors.toList());
    }

    @Override
    public List<FeaturedProductsResponse> getFeatured() {
        Page<Product> topProducts = productRepository.findTopRatedProducts(PageRequest.of(0, 8, Sort.unsorted()));
        List<Product> products = topProducts.getContent();
        return products.stream().map(FeaturedProductsResponse::mapFrom).collect(Collectors.toList());
    }
}
