package com.agile.identity_service.Controller;
import com.agile.dto.ResponseDto;
import com.agile.identity_service.DTO.UserDto;
import com.agile.identity_service.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

   @Autowired
    UserService userService;

@PostMapping("/register")
   public ResponseDto addUser(@RequestPart UserDto userDto, @RequestPart MultipartFile file) throws IOException {
    log.info("creating user");
       return userService.addUser(userDto, file);

   }

   @PostMapping("/login")
    public ResponseDto loginUser(@RequestBody UserDto userDto) {
         return userService.loginUser(userDto);
    }
}
