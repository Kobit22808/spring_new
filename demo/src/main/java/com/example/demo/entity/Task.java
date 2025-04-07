package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleTasks;
    private String textTasks;
    private LocalDate endingTasks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private User employer; // Лидер команды, назначающий задачу

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workman_id")
    private User workman; // Работник, которому назначена задача

    private String statusTasks;

    @ManyToOne // Добавляем связь с командой
    @JoinColumn(name = "comand_id") // Указываем имя колонки в таблице
    private Comand comand; // Связь с командой
}