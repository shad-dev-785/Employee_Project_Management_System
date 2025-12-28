package com.agile.project_service.config;

import com.agile.dto.ResponseDto;
import com.agile.project_service.dto.UserObject;
import jakarta.websocket.ClientEndpoint;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "identity-service")
public interface IdentityClient {
    @GetMapping("/user/find-by-id")
    ResponseDto getUserById(@RequestParam Long id);
}
