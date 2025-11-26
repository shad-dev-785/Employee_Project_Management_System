package com.agile.dto;

import com.agile.Position;
import com.agile.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @Column(unique = true)
    private String username;

    private String phone;
    private String city;
    @Column(unique = true)
    private String firstname;

    @Column(unique = true)
    private String lastName;

    private String image;
    private UserType type;
    private Position position;
    private String corporation;
    private String status;
    private String manager;
    private String skypeName;
    private String createdBy;
    @Column(unique = true)
    private String email;

    private String password;
}
