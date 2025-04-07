package com.example.demo.controller;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.ComandService;
import org.springframework.beans.factory.annotation.Autowired;
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
}