package com.backend_ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    boolean deleteImage(String imageName);
}