package com.agile.identity_service.Controller;
import com.agile.identity_service.DTO.ResponseDto;
import com.agile.identity_service.DTO.UserDto;
import com.agile.identity_service.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/find-by-id")
    @PreAuthorize("hasAnyRole('MANAGER', 'DEVELOPER')")
    public ResponseDto findUserById(@RequestParam Long id){
        log.info("Fetching user with id: {}", id);
        return userService.findById(id);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('MANAGER', 'DEVELOPER')")
    public ResponseDto updateUser(@RequestBody UserDto userDto){
        log.info("Updating user: {}", userDto);
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseDto deleteUser(@RequestParam Long id){
        log.info("Deleting user with id: {}", id);
        return userService.deleteUser(id);
    }


}
