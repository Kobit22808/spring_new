package com.example.demo.controller;

import com.example.demo.entity.TaskStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.example.demo.entity.Task;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ComandRepository comandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/create")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("comands", comandRepository.findAll()); // Получаем все команды
        model.addAttribute("users", userRepository.findAll()); // Получаем всех пользователей
        return "createTask"; // Возвращаем имя шаблона
    }

    @PostMapping("/create")
    public String createTask(@RequestParam String title,
                             @RequestParam String text,
                             @RequestParam Long userId,
                             @RequestParam Long comandId) {
        taskService.createTask(title, text, userId, comandId); // Передаем comandId
        return "redirect:/tasks"; // Перенаправляем
    }


    @GetMapping("/comand/{comandId}")
    public ResponseEntity<List<Task>> getTasksForComand(@PathVariable Long comandId) {
        List<Task> tasks = taskService.getTasksForComand(comandId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{id}/addTask")
    public String addTaskToComand(@PathVariable Long id,
                                  @RequestParam String taskTitle,
                                  @RequestParam String taskText,
                                  @RequestParam Long userId) {
        // Передаем id команды (id) в метод createTask
        taskService.createTask(taskTitle, taskText, userId, id); // Теперь передаем id команды
        return "redirect:/comands/" + id; // Перенаправляем на страницу команды
    }
    @PostMapping("/{id}/updateStatus")
    public String updateTaskStatus(@PathVariable Long id, @RequestParam String newStatus) {
        // Преобразуем строку в соответствующее значение перечисления
        TaskStatus status = TaskStatus.valueOf(newStatus.toUpperCase()); // Преобразуем строку в верхний регистр
        taskService.updateTaskStatus(id, status);
        return "redirect:/tasks"; // Перенаправляем на страницу задач
    }
}