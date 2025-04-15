package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.models.LoginRequest;
import com.example.demo.models.LoginResponse;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    private PostService postService;

    // регистрация
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
        //user.setPassword(userService.encodePassword(user.getPassword())); // Хеширование пароля
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

// вход в аккаунт
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Long userId = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            LoginResponse response = new LoginResponse(loginRequest.getUsername(), userId); // Теперь типы совпадают
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new LoginResponse(e.getMessage(), null));
        }
    }

    // создание команды
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()") // Проверка, что пользователь аутентифицирован
    public ResponseEntity<String> createComand(@RequestBody String title, @RequestParam String description, @RequestParam Long leaderId) {
        comandService.createComand(title, description, leaderId);
        return ResponseEntity.ok("Comand created"); // Перенаправляем на страницу команд
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



    /// //////
//    @GetMapping("/comands/{id}")
//    public Comand getComandById(@PathVariable Long id) {
//        return comandService.getComand(id);
//    }

//    @PostMapping
//    public Comand createComand(@RequestBody Comand comand) {
//        return comandService.createComand(comand.getTitle(), comand.getDescription(), comand.getLeader().getId());
//    }

//    @GetMapping("/{comandId}/posts")
//    public List<Post> getPostsByComand(@PathVariable Long comandId) {
//        Comand comand = comandService.getComand(comandId);
//        return postService.getPostsByComand(comand);
//    }

//    @PostMapping("/comands/{comandId}/posts")
//    public Post createPost(@PathVariable Long comandId, @RequestBody Post post) {
//        return postService.createPost(comandId, post);
//    }

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