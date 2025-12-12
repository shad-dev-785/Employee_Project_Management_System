package com.agile.identity_service.Service;

import com.agile.common.security.JwtUtil;
import com.agile.identity_service.Config.CustomUserDetails;
import com.agile.identity_service.DTO.ResponseDto;
import com.agile.identity_service.DTO.ResponseUserDto;
import com.agile.identity_service.DTO.UserDto;
import com.agile.identity_service.Entity.Status;
import com.agile.identity_service.Entity.User;
import com.agile.identity_service.Exception.EpmsException;
import com.agile.identity_service.Repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService service;

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
            user.setStatus(Status.ACTIVE);
        }
        User savedUser = userRepository.save(user);
        ResponseUserDto responseUserDto = new ResponseUserDto();
        mapper.map(savedUser, responseUserDto);
        dto.setMessage("User registered successfully with id: " + savedUser.getId());
        dto.setData(responseUserDto);
        return dto;
    }

    public ResponseDto loginUser(UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getEmail(), userDto.getPassword()
                    )
            );
            if (authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getUserEntity().getRole());

                User user= userDetails.getUserEntity();
                ResponseUserDto dto = new ResponseUserDto();
                mapper.map(user, dto);

                dto.setToken(token);

                responseDto.setMessage("User logged in successfully");
                responseDto.setData(dto);

            }
        } catch (Exception e) {
            e.printStackTrace();
            responseDto.setMessage(e.getMessage());
        }
        return responseDto;
    }


    public ResponseDto getUsers(UserDto filters) {
        ResponseDto responseDto = new ResponseDto();
        Pageable pageable= PageRequest.of(filters.getPage(), filters.getSize());
//        Using JpaSpecificationExecutor to apply filters
        Specification<User> spec= (root, query, cb) -> {
            List<Predicate> predicates= new ArrayList<>();
            if(filters.getName()!=null && !filters.getName().isEmpty()){
                predicates.add(cb.like(root.get("name"), "%" + filters.getName() + "%"));
            }
            if(filters.getEmail()!=null && !filters.getEmail().isEmpty()){
                predicates.add(cb.like(root.get("email"), ("%") + filters.getEmail()+ ("%")));
            }
            if(filters.getRole()!=null){
                predicates.add(cb.equal(root.get("role"), filters.getRole()));
            }
            if(filters.getStatus()!=null && !filters.getStatus().toString().isEmpty()){
                predicates.add(cb.equal(root.get("status"), filters.getStatus().toString()));
            }
            if(filters.getPhone()!=null && !filters.getPhone().isEmpty()){
                predicates.add(cb.like(root.get("phone"), ("%") + filters.getPhone()+ ("%")));
            }
             return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<User> users= userRepository.findAll(spec, pageable);
        if (users.isEmpty()) {
            responseDto.setMessage("No users found");
        } else {
            responseDto.setMessage("Users retrieved successfully");
            responseDto.setData(users.getContent());
        }
        return responseDto;
    }
@CachePut(value = "users", key = "#userDto.id")
    public ResponseDto updateUser(UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }

    public ResponseDto deleteUser(Long userId) {
        ResponseDto responseDto = new ResponseDto();
        return responseDto;
    }

}
