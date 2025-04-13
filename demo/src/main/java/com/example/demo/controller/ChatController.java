package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.models.Message;
import com.example.demo.service.ChatService;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send") // Слушаем сообщения на этой конечной точке
    public void send(Message message) {
        Message processedMessage = chatService.processMessage(message); // Обработка сообщения
        messagingTemplate.convertAndSend("/chat/messages", processedMessage); // Отправка сообщения всем клиентам
    }
}