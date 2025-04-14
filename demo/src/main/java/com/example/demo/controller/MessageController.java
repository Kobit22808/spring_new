package com.example.demo.controller;


import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages") // Указываем базовый путь для этого контроллера
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping
    public Message createMessage(@RequestParam Long userId, @RequestBody Message message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User  not found"));
        message.setUser (user);
        return messageRepository.save(message);
    }
}