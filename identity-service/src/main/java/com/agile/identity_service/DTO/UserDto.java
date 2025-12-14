package com.agile.identity_service.DTO;

import com.agile.identity_service.Entity.Status;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private Status status;
    private String profileImagePath;
    private int pageNo = 0;
    private int pageSize = 10;
}
