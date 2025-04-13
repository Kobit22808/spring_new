package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comand_id")
    private Comand comand; // Связь с командой

    private String formattedCreatedAt; // Отформатированная дата

    private String title; // Заголовок поста
    private String content; // Содержимое поста
    private LocalDateTime createdAt; // Дата и время создания поста

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images; // Список изображений

    private List<String> formattedImages; // Отформатированные изображения в виде строк Base64

    @Column(name = "comand_id", insertable = false, updatable = false)
    private Long comandId; // Поле для хранения идентификатора команды

    public Long getComandId() { // Геттер для командного идентификатора
        return comandId;
    }

    public void setComandId(Long comandId) { // Сеттер для командного идентификатора
        this.comandId = comandId;
    }
}
