package com.example.demo.entity;

public enum TaskStatus {
    NEW("Новая"),            // Новая задача
    IN_PROGRESS("В процессе выполнения"),    // В процессе выполнения
    ACCEPTED("Принята"),       // Принята
    COMPLETED("Завершена"),      // Завершена
    WAITING("Ожидание");        // Ожидание

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}