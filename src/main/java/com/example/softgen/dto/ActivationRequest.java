package com.example.softgen.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ActivationRequest {
    String email;
    String password;
    String token;
}
