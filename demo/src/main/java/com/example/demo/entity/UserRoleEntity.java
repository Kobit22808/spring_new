package com.example.demo.entity;

import jakarta.persistence.*;
import com.example.demo.entity.Role; // Убедитесь, что этот импорт есть
@Entity
@Table(name = "roles")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Role name; // Изменено на Role

    // Конструкторы, геттеры и сеттеры
    public UserRoleEntity() {}

    public UserRoleEntity(Role name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getName() {
        return name;
    }

    public void setName(Role name) {
        this.name = name;
    }
}