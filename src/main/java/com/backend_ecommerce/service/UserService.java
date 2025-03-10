package com.backend_ecommerce.service;

import com.backend_ecommerce.request.UpdatePasswordRequest;
import com.backend_ecommerce.request.UpdateUserRequest;
import com.backend_ecommerce.response.ProfileUserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String uploadAvatar(MultipartFile file);

    String updateUserHandler(UpdateUserRequest req);

    String updatePasswordHandler(UpdatePasswordRequest req);

    ProfileUserResponse getProfileHandler();

    String deleteUserById(Long id);

    String unlockUser(Long id);
}
