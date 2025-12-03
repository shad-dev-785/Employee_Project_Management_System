package com.agile.identity_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String role;
    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;
}
