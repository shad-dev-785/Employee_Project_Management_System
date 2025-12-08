package com.agile.identity_service.DTO;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private String profileImagePath;
}
