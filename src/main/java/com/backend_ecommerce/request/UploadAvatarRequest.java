package com.backend_ecommerce.request;

import com.backend_ecommerce.utils.image.ImageFile;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UploadAvatarRequest {
    @NotNull(message = "File is required")
    @ImageFile
    private MultipartFile file;
}
