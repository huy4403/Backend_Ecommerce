package com.backend_ecommerce.controller.user;

import com.backend_ecommerce.response.AllUserResponse;
import com.backend_ecommerce.response.ApiResponse;
import com.backend_ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser() {
        List<AllUserResponse> allUserResponse = userService.getAllUer();
        return ApiResponse.accepted("Get All User successfully", allUserResponse);
    }
}
