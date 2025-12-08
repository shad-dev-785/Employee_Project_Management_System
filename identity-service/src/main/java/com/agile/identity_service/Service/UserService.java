package com.agile.identity_service.Service;

import com.agile.identity_service.DTO.ResponseDto;
import com.agile.identity_service.DTO.ResponseUserDto;
import com.agile.identity_service.DTO.UserDto;
import com.agile.identity_service.Entity.User;
import com.agile.identity_service.Exception.EpmsException;
import com.agile.identity_service.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper mapper;

    @Value("${application.file.upload-dir}")
    private String uploadDir;

    @Autowired
    AuthenticationManager authenticationManager;

    public ResponseDto addUser(UserDto userDto, MultipartFile file) throws IOException {
        ResponseDto dto= new ResponseDto();

        if(userRepository.existsByEmail(userDto.getEmail())){
           throw new EpmsException("User already exists with email: " + userDto.getEmail());
        }
        User user = new User();
        mapper.map(userDto, user);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

//        save profile picture
        if(file!=null && !file.isEmpty()){
           Path uploadPath = Paths.get(uploadDir);
           if(!Files.exists(uploadPath)){
             Files.createDirectories(uploadPath);
           }
           String originalFilename = file.getOriginalFilename();
             String extension = originalFilename.contains(".") ?
                     originalFilename.substring(originalFilename.lastIndexOf(".")) : "jpg";

             String fileName = UUID.randomUUID() + extension;
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImagePath("uploads/" + fileName);
        }
        User savedUser = userRepository.save(user);
        ResponseUserDto responseUserDto = new ResponseUserDto();
        mapper.map(savedUser, responseUserDto);
        dto.setMessage("User registered successfully with id: " + savedUser.getId());
        dto.setData(responseUserDto);
        return dto;
    }

    public ResponseDto loginUser(UserDto userDto) {
       Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getEmail(), userDto.getPassword()
                )
        );
       ResponseUserDto responseUserDto= new ResponseUserDto();
       if(authentication.isAuthenticated()){
       }
           ResponseDto dto= new ResponseDto();
           dto.setMessage("User logged in successfully");
           return dto;
    }
}
