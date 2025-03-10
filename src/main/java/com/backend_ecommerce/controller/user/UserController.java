package com.backend_ecommerce.controller.user;

import com.backend_ecommerce.request.UpdatePasswordRequest;
import com.backend_ecommerce.request.UpdateUserRequest;
import com.backend_ecommerce.request.UploadAvatarRequest;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.response.ProfileUserResponse;
import com.backend_ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        ProfileUserResponse profile = userService.getProfileHandler();
        return ApiResponse.ok("Profile", profile);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatarForUser(@ModelAttribute @Valid UploadAvatarRequest req) {
        String source = userService.uploadAvatar(req.getFile());
        return ApiResponse.accepted("User avatar uploaded", source);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest req) {
        String message = userService.updateUserHandler(req);
        return ApiResponse.accepted(message, "ok");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest req) {
        String message = userService.updatePasswordHandler(req);
        return ApiResponse.accepted(message);
    }
}
