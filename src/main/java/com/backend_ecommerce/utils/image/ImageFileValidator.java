package com.backend_ecommerce.utils.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageFileValidator implements ConstraintValidator<ImageFile, Object> {

    @Override
    public void initialize(ImageFile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    private static final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp, image/svg");

    @Override
    public boolean isValid(Object data, ConstraintValidatorContext context) {
        if(!(data instanceof MultipartFile file)) return false;
        if (file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Please select an image file")
                    .addConstraintViolation();
            return false;
        }
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/") && ALLOWED_TYPES.contains(contentType);
    }
}
