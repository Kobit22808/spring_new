package com.example.demo.controller;

import com.example.demo.entity.Comand;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ComandService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comands")
public class ComandController {

    @Autowired
    private ComandService comandService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String getComands(Model model) {
        model.addAttribute("comands", comandService.getComands());
        return "comands"; // Возвращает имя шаблона для списка команд
    }

    @GetMapping("/{id}")
    public String getComand(@PathVariable Long id, Model model) {
        Comand comand = comandService.getComand(id);
        if (comand == null) {
            return "404"; // Возвращаем страницу 404, если команда не найдена
        }
        model.addAttribute("comand", comand);
        model.addAttribute("users", comandService.getUsersInComand(id));
        model.addAttribute("tasks", taskService.getTasks());

        return "comand"; // Возвращает имя шаблона для страницы команды
    }

    @GetMapping("/{comandId}/addMember")
    public String showAddMemberForm(@PathVariable Long comandId, Model model) {
        model.addAttribute("comandId", comandId);
        model.addAttribute("users", userRepository.findAll()); // Получаем всех пользователей
        return "addMember"; // Возвращаем имя шаблона
    }

    @PostMapping("/{comandId}/addMember")
    public String addMemberToComand(@PathVariable Long comandId, @RequestParam Long userId) {
        comandService.addMemberToComand(comandId, userId);
        return "redirect:/comands/" + comandId; // Перенаправляем на страницу команды
    }
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()") // Проверка роли
    public String createComand(Model model) {
        // Логика для создания команды
        return "createComand"; // Возвращает имя шаблона для создания команды
    }
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()") // Проверка, что пользователь аутентифицирован
    public String createComand(@RequestParam String title, @RequestParam String description, @RequestParam Long leaderId) {
        comandService.createComand(title, description, leaderId);
        return "redirect:/comands"; // Перенаправляем на страницу команд
    }

}