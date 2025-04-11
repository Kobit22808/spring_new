package com.example.demo.models;

public class LoginRequest {
    private String username;
    private String password;

    // Конструктор по умолчанию
    public LoginRequest() {
    }

    // Геттер для username
    public String getUsername() {
        return username;
    }

    // Сеттер для username
    public void setUsername(String username) {
        this.username = username;
    }

    // Геттер для password
    public String getPassword() {
        return password;
    }

    // Сеттер для password
    public void setPassword(String password) {
        this.password = password;
    }
}