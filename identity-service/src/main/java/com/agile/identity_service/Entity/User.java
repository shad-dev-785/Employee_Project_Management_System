package com.agile.identity_service.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String role;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    }
