package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // ID пользователя, которому назначена задача
    private String message; // Сообщение уведомления
    private LocalDateTime createdAt; // Время создания уведомления
    private boolean isRead; // Статус прочтения уведомления

    // Геттеры и сеттеры (если не используете Lombok)
}