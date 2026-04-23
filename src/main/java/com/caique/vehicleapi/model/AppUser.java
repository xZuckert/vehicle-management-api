package com.caique.vehicleapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles; // ROLE_USER / ROLE_ADMIN
}