package com.example.demo.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleEntity;
import com.example.demo.service.UserService;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/createUser")
    public String showCreateUserForm() {
        return "createUser"; // Имя HTML-файла без расширения
    }

    @PostMapping("/create/users")
    public String createUser(@RequestParam String username,
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

            Role role = Role.valueOf(roles.toUpperCase());
            UserRoleEntity userRoleEntity = userService.findOrCreateRole(role);

            User user = new User();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPatronymic(patronymic);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setBirth(birthDate);

            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(userRoleEntity);
            roleSet.add(userRole);

            user.setUserRoles(roleSet);

            userService.addUser(user);
            logger.info("User created successfully: {}", username);
            return "redirect:/createUser";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid role: " + roles);
            logger.error("Error creating user: Invalid role - {}", roles);
            return "createUser";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while creating the user: " + e.getMessage());
            logger.error("Error creating user: {}", e.getMessage());
            return "createUser";
        }
    }

    // Other methods remain unchanged...
}
