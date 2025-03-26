package com.backend_ecommerce.controller.dashboard;

import com.backend_ecommerce.response.DashboardResponse;
import com.backend_ecommerce.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${api.prefix}/admin/dashboard")
@RestController
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashBoardService;

    @GetMapping
    public ResponseEntity<?> dashboard() {
        DashboardResponse response = dashBoardService.getDashBoardOverview();
        return ResponseEntity.ok(response);
    }
}
