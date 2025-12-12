package com.agile.identity_service.Controller;

import com.agile.identity_service.DTO.ResponseDto;
import com.agile.identity_service.DTO.UserDto;
import com.agile.identity_service.Repository.UserRepository;
import com.agile.identity_service.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/search")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseDto getUsers(@RequestBody UserDto filters){
        log.info("Fetching users with filters: {}", filters);
        return userService.getUsers(filters);
    }

}
