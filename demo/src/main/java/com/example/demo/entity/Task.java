package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

    @Enumerated(EnumType.STRING)
    private TaskStatus statusTasks;

    @ManyToOne // Добавляем связь с командой
    @JoinColumn(name = "comand_id") // Указываем имя колонки в таблице
    private Comand comand; // Связь с командой

    public void setStatus(TaskStatus newStatus) {
        this.statusTasks = newStatus;
    }

}