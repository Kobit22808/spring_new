package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRoleEntity role;

    // Конструкторы, геттеры и сеттеры
    public UserRole() {}

    public UserRole(User user, UserRoleEntity role) {
        this.user = user;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser () {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public UserRoleEntity getRole() {
        return role;
    }
    public void setRole(UserRoleEntity role) {
        this.role = role;
    }

}