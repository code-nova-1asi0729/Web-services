package com.codenova.vigilia.shared.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * Allows the Vigilia web application to call the API from another origin
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200", "https://vigilia-frontend.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}