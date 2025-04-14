package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/chat") // Указываем базовый путь для этого контроллера
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User  not authenticated");
        }

        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            message.setUser (user.get());
            messageRepository.save(message);
        } else {
            throw new RuntimeException("User  not found");
        }

        return message;
    }

    @GetMapping
    public String getAllMessages() {
        // Здесь можно вернуть представление, например, "chat" для отображения чата
        return "chat"; // Это будет соответствовать файлу chat.html в папке static
    }
}
