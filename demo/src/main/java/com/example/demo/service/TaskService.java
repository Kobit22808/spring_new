package com.example.demo.service;

import com.example.demo.entity.Comand;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.entity.UserComandLink;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ComandRepository comandRepository;

    public Task createTask(String title, String text, Long comandId, Long userId) {
        Comand comand = comandRepository.findById(comandId)
                .orElseThrow(() -> new RuntimeException("Comand not found"));

        // Проверяем, является ли пользователь членом команды
        User workman = comand.getUserComandLinks().stream()
                .map(UserComandLink::getUser )
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User  is not a member of the comand"));

        Task task = new Task();
        task.setTitleTasks(title);
        task.setTextTasks(text);
        task.setEmployer(comand.getLeader()); // Лидер команды назначает задачу
        task.setWorkman(workman); // Назначаем задачу участнику команды
        task.setStatusTasks("Pending"); // Устанавливаем статус задачи

        return taskRepository.save(task);
    }

    public List<Task> getTasksForComand(Long comandId) {
        Comand comand = comandRepository.findById(comandId)
                .orElseThrow(() -> new RuntimeException("Comand not found"));
        return taskRepository.findByComand(comand);
    }
    public List<Task> getTasksByEmployer(User employer) {
        return taskRepository.findByEmployer(employer); // Получаем задачи работодателя
    }
    public List<Task> getTasksByWorkman(User workman) {
        return taskRepository.findByWorkman(workman); // Получаем задачи работника
    }
}