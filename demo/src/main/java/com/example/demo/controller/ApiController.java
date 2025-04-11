package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.models.LoginRequest;
import com.example.demo.models.LoginResponse;
import com.example.demo.service.AuthService;
import com.example.demo.service.ComandService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ComandService comandService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser (@RequestBody User user) {
        // Проверка существования пользователя
        try {
            userService.findByUsername(user.getUsername());
            return ResponseEntity.badRequest().body("User  already exists");
        } catch (RuntimeException e) {
            // Пользователь не найден, продолжаем регистрацию
        }

        // Создание нового пользователя
        user.setPassword(userService.encodePassword(user.getPassword())); // Хеширование пароля
        user.setCreatedAt(LocalDate.now());

        // Получение или создание роли USER
        UserRoleEntity userRole = userService.findOrCreateRole(Role.USER); // Используем перечисление

        // Создание набора ролей
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(user, userRole)); // Создаем объект UserRole

        user.setUserRoles(roles); // Устанавливаем роли для пользователя

        // Добавление нового пользователя
        userService.addUser (user);

        return ResponseEntity.ok("User  registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            String userId = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            LoginResponse response = new LoginResponse(loginRequest.getUsername(), userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new LoginResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/currentUser ")
    public ResponseEntity<User> getCurrentUser () {
        User currentUser  = userService.getCurrentUser ();
        if (currentUser  == null) {
            return ResponseEntity.status(401).build(); // Если пользователь не аутентифицирован
        }
        return ResponseEntity.ok(currentUser );
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task.getTitleTasks(), task.getTextTasks(), task.getWorkman().getId(), task.getComand().getId());
        return ResponseEntity.ok(createdTask);
    }

    @PostMapping("/tasks/{id}/updateStatus")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long id, @RequestParam String newStatus) {
        TaskStatus status = TaskStatus.valueOf(newStatus.toUpperCase());
        taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok("Task status updated successfully");
    }

    @PostMapping("/tasks/{id}/delete")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, @RequestParam Long userId) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @GetMapping("/comands")
    public ResponseEntity<List<Comand>> getComands() {
        List<Comand> comands = comandService.getComands();
        return ResponseEntity.ok(comands);
    }

    @GetMapping("/comands/{id}")
    public ResponseEntity<Comand> getComand(@PathVariable Long id) {
        Comand comand = comandService.getComand(id);
        return ResponseEntity.ok(comand);
    }

//    @PostMapping("/comands")
//    public ResponseEntity<Comand> createComand(@RequestBody Comand comand) {
//        Comand createdComand = comandService.createComand(comand.getName(), comand.getDescription());
//        return ResponseEntity.ok(createdComand);
//    }
//
//    @PostMapping("/comands/{id}/update")
//    public ResponseEntity<String> updateComand(@PathVariable Long id, @RequestBody Comand comand) {
//        comandService.updateComand(id, comand);
//        return ResponseEntity.ok("Comand updated successfully");
//    }
//
//    @PostMapping("/comands/{id}/delete")
//    public ResponseEntity<String> deleteComand(@PathVariable Long id) {
//        comandService.deleteComand(id);
//        return ResponseEntity.ok("Comand deleted successfully");
//    }
}