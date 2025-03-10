package com.backend_ecommerce.controller.user;

import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/admin/user")
public class AdminUserController {

    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        String message = userService.deleteUserById(id);
        return ApiResponse.noContent(message);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> unlockUser(@PathVariable Long id) {
        String message = userService.unlockUser(id);
        return ApiResponse.accepted(message);
    }
}
