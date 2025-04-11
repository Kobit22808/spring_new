package com.example.demo.models;

public class LoginResponse {
    private String username;
    private String userId;

    public LoginResponse(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }
}