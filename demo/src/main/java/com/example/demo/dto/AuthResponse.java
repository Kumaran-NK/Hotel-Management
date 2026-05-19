package com.example.demo.dto;
import lombok.Data;

@Data
public class AuthResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String message;

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.message = "Success";
    }
}