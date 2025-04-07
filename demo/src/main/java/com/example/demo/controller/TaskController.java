package com.example.demo.controller;

import org.springframework.ui.Model;
import com.example.demo.dto.TaskDto;
import com.example.demo.entity.Task;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
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
    public String createTask(@RequestParam String title, @RequestParam String text, @RequestParam Long comandId, @RequestParam Long userId) {
        taskService.createTask(title, text, comandId, userId);
        return "redirect:/tasks"; // Перенаправляе
    }

    @GetMapping("/comand/{comandId}")
    public ResponseEntity<List<Task>> getTasksForComand(@PathVariable Long comandId) {
        List<Task> tasks = taskService.getTasksForComand(comandId);
        return ResponseEntity.ok(tasks);
    }
}