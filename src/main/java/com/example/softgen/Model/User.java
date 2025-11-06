package com.example.softgen.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String activationToken;
    private boolean activate = false;

    private String role = "USER";


}
