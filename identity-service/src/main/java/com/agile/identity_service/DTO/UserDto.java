package com.agile.identity_service.DTO;

import com.agile.identity_service.Entity.Status;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private Status status;
    private String profileImagePath;

    @DefaultValue("0")
    private int page;

    @DefaultValue("10")
    private int size;
}
