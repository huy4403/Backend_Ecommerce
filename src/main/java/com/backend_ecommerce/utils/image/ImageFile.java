package com.backend_ecommerce.utils.image;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageFileValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFile {
    String message() default "Invalid file format. Please upload a JPEG, PNG, GIF or WEBP file.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
