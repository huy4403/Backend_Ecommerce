package com.backend_ecommerce;

import com.backend_ecommerce.domain.ROLE_NAME;
import com.backend_ecommerce.model.*;
import com.backend_ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeData();
        initializeUser();
    }

    private void initializeData() {
        List<Category> categories = List.of(
                new Category(null, "Điện thoại", null, 1),
                new Category(null, "Laptop", null, 1),
                new Category(null, "Phụ kiện", null, 1)
        );
        categoryRepository.saveAll(categories);


        List<Product> products = new ArrayList<>();
        List<String> imageLinks = List.of(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg"
        );

        for (int i = 1; i <= 10; i++) {
            Category category = categories.get(i % categories.size());
            Product product = new Product();
            product.setTitle("Sản phẩm " + i);
            product.setDescription("Mô tả sản phẩm " + i);
            product.setCategory(category);
            product.setImages(imageLinks);
            product.setCreatedAt(LocalDateTime.now());
            products.add(product);
        }
        productRepository.saveAll(products);

        List<ProductVariant> variants = new ArrayList<>();
        for (Product product : products) {
            Attribute color = new Attribute(null, "Màu sắc", new ArrayList<>(), product);
            Attribute storage = new Attribute(null, "Dung lượng", new ArrayList<>(), product);
            attributeRepository.saveAll(List.of(color, storage));

            List<AttributeValue> colorValues = List.of(
                    new AttributeValue(null, color, "Đỏ"),
                    new AttributeValue(null, color, "Xanh"),
                    new AttributeValue(null, color, "Đen")
            );
            List<AttributeValue> storageValues = List.of(
                    new AttributeValue(null, storage, "64GB"),
                    new AttributeValue(null, storage, "128GB"),
                    new AttributeValue(null, storage, "256GB")
            );
            attributeValueRepository.saveAll(colorValues);
            attributeValueRepository.saveAll(storageValues);
            for (AttributeValue colorValue : colorValues) {
                for (AttributeValue storageValue : storageValues) {
                    ProductVariant variant = new ProductVariant();
                    variant.setProduct(product);
                    variant.setAttributeValues(List.of(colorValue, storageValue));
                    variants.add(variant);
                }
                productVariantRepository.saveAll(variants);
            }
        }
    }

    private void initializeUser() {
        List<User> users = new ArrayList<>();
        User admin = new User();
        admin.setFullName("Doan Huy");
        admin.setEmail("huy4403nd@gmail.com");
        admin.setPassword(passwordEncoder.encode("4403"));
        admin.setRole(ROLE_NAME.ADMIN);
        users.add(admin);

        User user = new User();
        user.setFullName("Huy");
        user.setEmail("doanhuy0168@gmail.com");
        user.setPassword(passwordEncoder.encode("4403"));
        user.setRole(ROLE_NAME.USER);
        users.add(user);

        userRepository.saveAll(users);
    }
}
