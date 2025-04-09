package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
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

    @Autowired
    private UserComandLinkRepository userComandLinkRepository;

    public boolean existsUserComandLink(User user, Comand comand) {
        return userComandLinkRepository.findByUserAndComand(user, comand).isPresent();
    }

    public Task createTask(String title, String text, Long userId, Long comandId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Пользователь с ID " + userId + " не существует");
        }

        // Находим работника по его ID
        User workman = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + userId + " не существует"));

        // Находим команду по ее ID
        Comand comand = comandRepository.findById(comandId)
                .orElseThrow(() -> new RuntimeException("Команда с ID " + comandId + " не существует"));

        // Проверяем, существует ли связь между пользователем и командой
        if (!existsUserComandLink(workman, comand)) {
            throw new RuntimeException("Пользователь с ID " + userId + " не является участником команды с ID " + comandId);
        }
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

    // Метод для редактирования задачи
    public Task updateTask(Long taskId, String title, String text, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        // Проверяем, является ли текущий пользователь работодателем
        if (!task.getEmployer().getId().equals(userId)) {
            throw new RuntimeException("Только работодатель может редактировать задачу");
        }

        // Обновляем поля задачи
        task.setTitleTasks(title);
        task.setTextTasks(text);

        // Сохраняем обновленную задачу
        return taskRepository.save(task);
    }

    // Метод для удаления задачи
    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));

        // Проверяем, является ли текущий пользователь работодателем
        if (!task.getEmployer().getId().equals(userId)) {
            throw new RuntimeException("Только работодатель может удалить задачу");
        }

        taskRepository.delete(task);
    }
    public Task findById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача с ID " + taskId + " не найдена"));
    }

}