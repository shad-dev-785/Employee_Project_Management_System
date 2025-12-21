package com.agile.identity_service;

import com.agile.identity_service.Config.GlobalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@EnableCaching
@SpringBootApplication(scanBasePackages = {
		"com.agile" // 1. Scan this service's own code
		   // 2. Scan the Shared Library code
})
public class IdentityServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}

}
