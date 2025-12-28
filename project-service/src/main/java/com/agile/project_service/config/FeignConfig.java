package com.agile.project_service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return template -> {
            // Example: Add a custom header to all outgoing Feign requests
            ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attribute != null) {
                String authHeader = attribute.getRequest().getHeader("Authorization");
                if (authHeader != null) {
                    template.header("Authorization", authHeader);
                }
            }
        };
    }
}
