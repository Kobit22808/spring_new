package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleEntity;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "home"; // имя файла home.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // имя файла login.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // имя файла register.html
    }

    @PostMapping("/register")
    public String registerUser (@RequestParam String username,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String patronymic,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam LocalDate birth,
                                Model model) {
        // Проверка существования пользователя
        try {
            userService.findByUsername(username);
            model.addAttribute("error", "User  already exists");
            return "register"; // Вернуть на страницу регистрации с ошибкой
        } catch (RuntimeException e) {
            // Пользователь не найден, продолжаем регистрацию
        }

        // Создание нового пользователя
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPatronymic(patronymic);
        user.setEmail(email);
        user.setPassword(password); // Не забудьте хешировать пароль перед сохранением
        user.setBirth(birth);
        user.setCreatedAt(LocalDate.now());

        // Получение или создание роли USER
        UserRoleEntity userRole = userService.findOrCreateRole(Role.USER); // Используем перечисление

        // Создание набора ролей
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(user, userRole)); // Создаем объект UserRole

        user.setUserRoles(roles); // Устанавливаем роли для пользователя

        // Добавление нового пользователя
        userService.addUser(user);

        // Перенаправление на страницу входа после успешной регистрации
        return "redirect:/login";
    }
}
