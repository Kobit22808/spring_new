package com.example.demo.controller;

import com.example.demo.entity.TaskStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ComandRepository comandRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

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
        return "redirect:/comand/{comandId}"; // Перенаправляем
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
                                  @RequestParam Long userId, Model model) {
        // Передаем id команды (id) в метод createTask
        try {
            Task task = taskService.createTask(taskTitle, taskText, userId, id); // Теперь передаем id команды
            model.addAttribute("task", task);
            return "redirect:/comands/" + id; // Перенаправляем на страницу команды
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

    }
    @PostMapping("/{id}/updateStatus")
    public String updateTaskStatus(@PathVariable Long id, @RequestParam String newStatus) {
        // Преобразуем строку в соответствующее значение перечисления
        TaskStatus status = TaskStatus.valueOf(newStatus.toUpperCase()); // Преобразуем строку в верхний регистр
        taskService.updateTaskStatus(id, status);
        return "redirect:/"; // Перенаправляем на страницу задач
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.findById(id); // Получаем задачу по ID
        if (task == null) {
            System.out.println("Задача не найдена с ID: " + id);
            return "redirect:/"; // Перенаправляем на страницу со списком задач, если задача не найдена
        }
        model.addAttribute("task", task); // Добавляем задачу в модель
        System.out.println("Задача найдена с ID: " + id);
        User currentUser  = userService.getCurrentUser (); // Предполагается, что у вас есть метод для получения текущего пользователя
        model.addAttribute("user_id", currentUser  != null ? currentUser .getId() : null); // Добавляем ID текущего пользователя
        return "editTask"; // Возвращаем имя шаблона для редактирования задачи
    }

    @PostMapping("/{id}/edit")
    public String editTask(@PathVariable Long id, @RequestParam String title, @RequestParam String text, @RequestParam Long userId) {
        System.out.println("Editing task with ID: " + id);
        System.out.println("New title: " + title);
        System.out.println("New text: " + text);
        System.out.println("User  ID: " + userId);

        taskService.updateTask(id, title, text, userId);
        return "redirect:/"; // Перенаправление на страницу со списком задач
    }

    // Метод для удаления задачи
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, @RequestParam Long userId) {
        System.out.println("Deleting task with ID: " + id + " for user ID: " + userId);
        taskService.deleteTask(id, userId);
        return "redirect:/"; // Перенаправление на страницу со списком задач
    }
}