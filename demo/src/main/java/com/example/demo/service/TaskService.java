package com.example.demo.service;

import com.example.demo.entity.Comand;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.entity.UserComandLink;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ComandRepository comandRepository;

    @Autowired
    private UserRepository userRepository;

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
        task.setStatusTasks("Ожидание"); // Устанавливаем статус задачи

        // Сохраняем задачу в базе данных
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

}