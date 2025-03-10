package com.backend_ecommerce.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary configKey() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "your cloud name");
        config.put("api_key", "Your api key");
        config.put("api_secret", "Your api secret");
        return new Cloudinary(config);
    }
}