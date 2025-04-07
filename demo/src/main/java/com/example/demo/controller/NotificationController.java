package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService; // Сервис для получения текущего пользователя

    @GetMapping("/notifications")
    public String getNotifications(Model model) {
        User currentUser  = userService.getCurrentUser (); // Получаем текущего пользователя
        System.out.println("Текущий пользователь: " + currentUser );

        // Получаем уведомления для текущего пользователя
        List<Notification> notifications = notificationRepository.findByUserId(currentUser .getId());
        model.addAttribute("notifications", notifications); // Передаем список объектов Notification в модель

        return "notifications"; // Возвращает имя шаблона для отображения уведомлений
    }

    @PostMapping("/notifications/read/{id}")
    public String markAsRead(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Уведомление не найдено"));
        notification.setRead(true); // Обновляем статус уведомления
        notificationRepository.save(notification); // Сохраняем изменения
        return "redirect:/notifications"; // Перенаправляем обратно на страницу уведомлений
    }
}