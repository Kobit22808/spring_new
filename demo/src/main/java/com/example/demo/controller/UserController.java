package com.example.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleEntity;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/createUser ")
    public String showCreateUserForm() {
        return "createUser "; // Имя HTML-файла без расширения
    }

    @PostMapping("/api/users")
    public String createUser (@RequestParam String username,
                              @RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String patronymic,
                              @RequestParam String email,
                              @RequestParam String password,
                              @RequestParam String birth,
                              @RequestParam String roles,
                              Model model) {
        try {
            LocalDate birthDate = LocalDate.parse(birth);
            Set<UserRole> roleSet = new HashSet<>();

            // Преобразование строки в перечисление Role
            Role role = Role.valueOf(roles.toUpperCase()); // Преобразуем строку в значение перечисления
            UserRoleEntity userRoleEntity = userService.findOrCreateRole(role); // Получаем или создаем роль

            // Создание нового пользователя
            User user = new User();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPatronymic(patronymic);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password)); // Хеширование пароля
            user.setBirth(birthDate);

            // Создание объекта UserRole и добавление его в набор ролей
            UserRole userRole = new UserRole(); // Создаем объект UserRole
            userRole.setUser (user); // Устанавливаем пользователя
            userRole.setRole(userRoleEntity); // Устанавливаем роль
            roleSet.add(userRole); // Добавляем в набор ролей

            user.setUserRoles(roleSet); // Устанавливаем роли для пользователя

            // Добавление нового пользователя
            userService.addUser (user); // Теперь передаем объект User

            return "redirect:/createUser "; // Перенаправление на страницу после создания
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid role: " + roles);
            return "createUser "; // Вернуть на страницу с ошибкой
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while creating the user: " + e.getMessage());
            return "createUser "; // Вернуть на страницу с ошибкой
        }
    }
}