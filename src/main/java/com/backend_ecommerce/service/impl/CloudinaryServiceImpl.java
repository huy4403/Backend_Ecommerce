package com.backend_ecommerce.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.backend_ecommerce.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if(filename == null) {
                log.info("Method uploadImage");
                throw new IllegalArgumentException("Tên file không hợp lệ");
            }
            String publicValue = generatePublicValue(filename);
            log.info("publicValue is: {}", publicValue);
            String extension = getFileName(filename)[1];
            log.info("extension is: {}", extension);

            boolean isSvg = "svg".equals(extension);
            if (isSvg) {
                extension = "png";
            }

            File fileUpload = convert(file);
            log.info("fileUpload is: {}", fileUpload);

            try {
                Map<String, Object> options = new HashMap<>();
                options.put("public_id", publicValue);
                if (isSvg) {
                    options.put("format", "png");
                }
                cloudinary.uploader().upload(fileUpload, options);
            } finally {
                cleanDisk(fileUpload);
            }

            return cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));
        } catch (IOException e) {
            throw new RuntimeException("Đã xảy ra lỗi khi upload ảnh");
        }
    }
    @Override
    public boolean deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId == null) return false;
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        if(file.getOriginalFilename() == null) {
            log.info("Method covert");
            throw new IllegalArgumentException("Đã xảy ra lỗi khi up ảnh");
        }
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()),
                getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }


    private void cleanDisk(File file) {
        try {
            log.info("file.toPath(): {}", file.toPath());
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }

    //For delete
    private String extractPublicId(String imageUrl) {
        Pattern pattern = Pattern.compile("/upload/v\\d+/(.*)\\.");
        Matcher matcher = pattern.matcher(imageUrl);
        return matcher.find() ? matcher.group(1) : null;
    }
}
