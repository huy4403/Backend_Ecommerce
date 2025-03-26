package com.backend_ecommerce.config;

import com.backend_ecommerce.filter.AccessDeniedEntryPoint;
import com.backend_ecommerce.filter.AuthEntryPoint;
import com.backend_ecommerce.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    private final String apiPrefix = "/api/v1";

    private final String[] PUBLIC_ENDPOINTS = {
            String.format("%s/auth/**", apiPrefix),
            String.format("%s/public/**", apiPrefix),
            String.format("%s/payment/*/status", apiPrefix),
            String.format("%s/payment/ipn", apiPrefix)
    };

    private final String[] ADMIN_ENDPOINTS = {
            String.format("%s/admin/**", apiPrefix)
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthEntryPoint authEntryPoint,
                                                   AccessDeniedEntryPoint accessDeniedEntryPoint) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(req
                        -> req.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
                        .anyRequest().authenticated())

                .sessionManagement(sessionManagement
                        -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(handler
                        -> handler.authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedEntryPoint))

                .cors(cors->cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOriginPatterns(Collections.singletonList("*"));
            cfg.setAllowedMethods(Collections.singletonList("*"));
            cfg.setAllowCredentials(true);
            cfg.setAllowedHeaders(Collections.singletonList("*"));
            cfg.setExposedHeaders(Collections.singletonList("*"));
            cfg.setMaxAge(3600L);
            return cfg;
        };
    }
}
