package com.agile.service;


import com.agile.constant.Constants;
import com.agile.constant.LeaveType;
import com.agile.dto.AuthenticationRequest;
import com.agile.dto.AuthenticationResponse;
import com.agile.dto.ResponseDto;
import com.agile.dto.SignupRequest;
import com.agile.model.Role;
import com.agile.model.User;
import com.agile.model.UserLeave;
import com.agile.repository.RoleRepository;
import com.agile.repository.UserLeaveRepository;
import com.agile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserLeaveRepository userLeaveRepository;

    public ResponseDto createUser(SignupRequest signupRequest) {
        ResponseDto responseDto= new ResponseDto();
        Optional<User> userWithUniqueEmail= Optional.ofNullable(userRepository.findByEmail(signupRequest.getEmail()));
        User userWithUniquePhone= userRepository.getUserByPhone(signupRequest.getPhone()).orElse(null);
        User userWithUniqueUserName= userRepository.getUserByUserName(signupRequest.getUsername()).orElse(null);
        if(userWithUniqueEmail.isPresent()){
            responseDto.setMessage("Email already Present!");
            responseDto.setStatusCode(Constants.EMAIL_NOT_UNIQUE_CODE);
            responseDto.setStatus(Constants.EMAIL_NOT_UNIQUE);
            return responseDto;
        } else if (userWithUniqueUserName!=null) {
            responseDto.setMessage("UserName already Present!");
            responseDto.setStatusCode(Constants.USERNAME_NOT_UNIQUE_CODE);
            responseDto.setStatus(Constants.USERNAME_NOT_UNIQUE);
            return responseDto;
        } else if (userWithUniquePhone!=null) {
            responseDto.setMessage("Phone Number already Present!");
            responseDto.setStatusCode(Constants.PHONE_NOT_UNIQUE_CODE);
            responseDto.setStatus(Constants.PHONE_NOT_UNIQUE);
            return responseDto;
        }
        User user= new User();
        user.setFirstname(signupRequest.getFirstname());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setCity(signupRequest.getCity());
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setLastName(signupRequest.getLastName());
        user.setUsername(signupRequest.getUsername());
        user.setCreatedBy(signupRequest.getCreatedBy());
        user.setCorporation(signupRequest.getCorporation());
        user.setManager(signupRequest.getManager());
        user.setImage(signupRequest.getImage());
        user.setPosition(signupRequest.getPosition());
        user.setSkypeName(signupRequest.getSkypeName());
        user.setStatus(signupRequest.getStatus());
        user.setType(signupRequest.getType());
        Role role= roleRepository.findByName(signupRequest.getType().name()).orElse(null);
        if(role==null){
            responseDto.setData(null);
            responseDto.setStatus(Constants.FAILED);
            responseDto.setStatusCode(Constants.FAILED_CODE);
            responseDto.setMessage("Failed to create user as Role not Found!");
            return responseDto;
        }
        user.setRole(role);
        User savedUser=userRepository.save(user);
        UserLeave userLeave= new UserLeave();
        userLeave.setUserId(savedUser.getId());
        userLeave.setSl(3);
        userLeave.setPl(12);
        userLeave.setEl(2);
        userLeave.setCl(3);
        userLeaveRepository.save(userLeave);
        responseDto.setData(savedUser);
        responseDto.setMessage("User Successfully Created");
        return responseDto;
    }


    public AuthenticationResponse generateResponse(AuthenticationRequest authenticationRequest) {
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(authenticationRequest.getEmail()));
        AuthenticationResponse authenticationResponse= new AuthenticationResponse();
        authenticationResponse.setUser(user.get());
        return authenticationResponse;
    }


    public ResponseDto updatePassword(Long userId, String oldPassword, String newPassword) {
        ResponseDto responseDto= new ResponseDto();
        User user= userRepository.findById(userId).orElse(null);
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
        String encryptedPassword= user.getPassword();
        if(encoder.matches(oldPassword, encryptedPassword)){
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            User savedUser= userRepository.save(user);
            responseDto.setMessage("Password updated Successfully");
            responseDto.setData(savedUser);
            return responseDto;
        }

        responseDto.setStatus(Constants.FAILED);
        responseDto.setMessage("Current password Mismatch! Enter Again!");
        responseDto.setStatusCode(Constants.FAILED_CODE);
        return responseDto;
    }


    public ResponseDto checkIfUserExists(SignupRequest signupRequest) {
        ResponseDto responseDto= new ResponseDto();
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(signupRequest.getEmail()));
        if(user.isPresent()){
                responseDto.setMessage("User is Present");
                responseDto.setData(user.get());
                return responseDto;
        }
        responseDto.setData(null);
        responseDto.setMessage("User not Found");
        responseDto.setStatus(Constants.USER_NOT_FOUND);
        responseDto.setStatusCode(Constants.USER_NOT_FOUND_CODE);
        return responseDto;
        }


    public ResponseDto fetchUsers() {
        ResponseDto responseDto= new ResponseDto();
        List<User> users = userRepository.findAll();
        responseDto.setData(users);
        responseDto.setMessage("Users");
        return responseDto;
    }

    public ResponseDto updateUser(Long userId, SignupRequest signupRequest) {
        ResponseDto responseDto= new ResponseDto();
        User user= userRepository.findById(userId).orElse(null);
        user.setFirstname(signupRequest.getFirstname());
        user.setCreatedAt(LocalDateTime.now());
        user.setCity(signupRequest.getCity());
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setLastName(signupRequest.getLastName());
        user.setUsername(signupRequest.getUsername());
        user.setCorporation(signupRequest.getCorporation());
        user.setManager(signupRequest.getManager());
        user.setImage(signupRequest.getImage());
        user.setPosition(signupRequest.getPosition());
        user.setSkypeName(signupRequest.getSkypeName());
        user.setStatus(signupRequest.getStatus());
        user.setType(signupRequest.getType());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser=userRepository.save(user);
        responseDto.setData(savedUser);
        responseDto.setMessage("User Successfully Updated");
        return responseDto;
    }

}
