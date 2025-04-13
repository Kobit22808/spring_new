package com.example.demo.models;

public class LoginResponse {
    private String username;
    private Long userId; // Измените на Long

    public LoginResponse(String username, Long userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() { // Измените возвращаемый тип на Long
        return userId;
    }
}