package com.backend_ecommerce.filter;

import com.backend_ecommerce.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend_ecommerce.config.JwtProvider;
import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.response.ErrorDetailsResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            //Sub 'Bearer ' to get token
            String token = header.substring(7);

            if (tokenService.isTokenRevoked(token)) {
                handleException(response, "Token has been revoked. Please login again.");
                return;
            }

            //Get user information from token
            String email = jwtProvider.getEmailFromToken(token);
            //Check email and user exist from Security context
            if(SecurityContextHolder.getContext().getAuthentication() == null) {

                UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(email);

                if(!userDetails.isEnabled()) {

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
                    errorDetails.setError("Your account has been banned.");
                    errorDetails.setDetails("JWT authentication failed");
                    errorDetails.setTimestamp(LocalDateTime.now());

                    response.getWriter().write(objectMapper.writeValueAsString(errorDetails));

                    return;
                }

                //Set user into security context holder
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            //Catch
        } catch (ExpiredJwtException e) {
            handleException(response, "Token is expired!");
            return;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            handleException(response, "Please login!");
            return;
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage(), e);
            handleException(response, "Please login!");
            return;
        }

        //Filter chain
        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse();
        errorDetails.setError(message);
        errorDetails.setDetails("JWT authentication failed");
        errorDetails.setTimestamp(LocalDateTime.now());

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }

}
