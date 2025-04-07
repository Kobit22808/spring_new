package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ComandRepository comandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public Task createTask(String title, String text, Long userId, Long comandId) {
        // Находим работника по его ID
        User workman = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Работник не найден"));

        // Находим команду по ее ID
        Comand comand = comandRepository.findById(comandId)
                .orElseThrow(() -> new RuntimeException("Команда не найдена"));

        // Создаем новую задачу
        Task task = new Task();
        task.setTitleTasks(title);
        task.setTextTasks(text);
        task.setEmployer(comand.getLeader()); // Устанавливаем лидера из команды
        task.setWorkman(workman); // Назначаем задачу работнику
        task.setStatusTasks(TaskStatus.NEW); // Устанавливаем статус задачи

        // Сохраняем задачу в базе данных
        Task savedTask = taskRepository.save(task);

        // Создание уведомления
        Notification notification = new Notification();
        notification.setUserId(workman.getId()); // Устанавливаем ID работника
        notification.setMessage("Вам назначена новая задача: " + savedTask.getTitleTasks());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false); // Уведомление новое
        notificationRepository.save(notification); // Сохраняем уведомление

        return savedTask; // Возвращаем сохраненную задачу
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

    public List<Task> getTasks() {
        return taskRepository.findAll(); // Возвращает все задачи
    }

    public void addTaskToComand(Long comandId, String taskTitle) {
        // Логика добавления задачи в команду
    }
    public List<Task> getTasksForWorkman(Long userId) {
        User workman = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Работник не найден"));
        return taskRepository.findByWorkman(workman); // Получаем задачи для работника
    }
    public void updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        task.setStatusTasks(newStatus); // Устанавливаем новый статус
        taskRepository.save(task);
    }


}