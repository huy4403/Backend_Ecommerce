package com.backend_ecommerce.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend_ecommerce.response.ErrorDetailsResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AccessDeniedEntryPoint implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AccessDeniedEntryPoint.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        logger.warn("Access denied for user: {}", request.getRemoteUser());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError("You aren't allowed to access this resource");
        errorDetails.setDetails(request.getRequestURI());
        errorDetails.setTimestamp(LocalDateTime.now());

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
