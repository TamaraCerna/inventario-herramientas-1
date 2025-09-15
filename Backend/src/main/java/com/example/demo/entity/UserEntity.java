package com.example.demo.entity;

import com.example.demo.entity.enums.Roler;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // sustituye el getter y setter
@NoArgsConstructor   // Constructor vacío
@AllArgsConstructor  // Constructor con todos los campos

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id autoincremental
    private int userId;
    @Column(nullable = false, unique = true, length = 100)
    private String userName;

    @Column(nullable = false, unique = true, length = 150)
    private String userEmail;

    @Column(nullable = false, length = 255)
    private String userPassword;
    @Enumerated(EnumType.STRING)
    public Roler userType;

}

