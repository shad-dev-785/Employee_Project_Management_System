package com.agile.project_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableCaching
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication(scanBasePackages = {
		"com.agile.project_service",  // 1. Scan this service's own code
		"com.agile.common_security"    // 2. Scan the Shared Library code
})
public class ProjectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectServiceApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth==null || !auth.isAuthenticated()) {
				return Optional.ofNullable("SYSTEM");
			}
			return Optional.of(auth.getName());
		};
	}
}

