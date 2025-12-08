package com.agile.identity_service.DTO;

import com.agile.identity_service.Entity.Status;
import lombok.Data;

@Data
public class ResponseUserDto {
    private Long Id;
    private String name;
    private String email;
    private String role;
    private Status status;
    private String phone;
}
