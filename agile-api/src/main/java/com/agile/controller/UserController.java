package com.agile.controller;


import com.agile.dto.AuthenticationRequest;
import com.agile.dto.AuthenticationResponse;
import com.agile.dto.ResponseDto;
import com.agile.dto.SignupRequest;
import com.agile.model.User;
import com.agile.service.AttendanceService;
import com.agile.service.UserServiceImpl;
import com.agile.service.jwt.UserDetailsServiceImpl;
import com.agile.utility.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping(path = "/api/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @PostMapping("/create-user")
    public ResponseDto createUser(@RequestBody SignupRequest signupRequest){
        log.info("Entering addUser {}",signupRequest.toString());
        return userService.createUser(signupRequest);
    }
    @PostMapping("/authentication")
    public ResponseDto createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws BadCredentialsException, DisabledException, UsernameNotFoundException, java.io.IOException {
        ResponseDto responseDto= new ResponseDto();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        }catch(BadCredentialsException ex){
            throw new BadCredentialsException("Email or Password Incorrect!");
        }catch (DisabledException disabledException){
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Register User First");
            return null;
        }
        final UserDetails userDetails= userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt= jwtUtil.generateToken(userDetails.getUsername());
        AuthenticationResponse authenticationResponse= userService.generateResponse(authenticationRequest);
        authenticationResponse.setToken(jwt);
        responseDto.setMessage("Login Successful");
        responseDto.setData(authenticationResponse);
        return responseDto;
    }
    @PostMapping("/update-password")
    public ResponseDto updatePassword(@RequestParam Long userId, @RequestParam String oldPassword, @RequestParam String newPassword){
        return userService.updatePassword(userId, oldPassword, newPassword);
    }
    @PutMapping("/update-user")
    public ResponseDto updateUser(@RequestParam Long userId, @RequestBody SignupRequest signupRequest){
        return userService.updateUser(userId,signupRequest);
    }
    @GetMapping("/retrieve")
    public ResponseDto checkIfUsernameExists(@RequestBody SignupRequest signupRequest){
        return userService.checkIfUserExists(signupRequest);
    }
    @PostMapping("/fetch")
    public ResponseDto fetchUsers(){
        return userService.fetchUsers();
    }
    ////    todo- updateUser, updatePassword, getUsers, username/email exists, fetch()- fetch users based on email, username, firsname, lastname, phone

}

