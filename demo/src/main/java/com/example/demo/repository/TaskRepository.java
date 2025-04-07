package com.example.demo.repository;

import com.example.demo.entity.Comand;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByComand(Comand comand); // Метод для поиска задач по команде
    List<Task> findByEmployer(User employer); // Метод для поиска задач по работодателю
    List<Task> findByWorkman(User workman); // Метод для поиска задач по работнику
}