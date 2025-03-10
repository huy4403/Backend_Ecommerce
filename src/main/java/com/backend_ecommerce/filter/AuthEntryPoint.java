package com.backend_ecommerce.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend_ecommerce.response.ErrorDetailsResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError("Please login!");
        errorDetails.setDetails(request.getRequestURI());
        errorDetails.setTimestamp(LocalDateTime.now());
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
