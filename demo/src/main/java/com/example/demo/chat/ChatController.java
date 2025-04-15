package com.example.demo.chat;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping; // Added import for Payload
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate; // Fixed variable declaration
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    @SendToUser ("/queue/messages")
    public void processMessage(@Payload ChatMessage message, Principal principal) {
        // Убедитесь, что principal не равен null
        if (principal == null) {
            throw new IllegalArgumentException("User  must not be null");
        }

        // Установите senderId и recipientId
        message.setSenderId(principal.getName()); // Установите ID отправителя
        // Убедитесь, что recipientId установлен в сообщении
        if (message.getRecipientId() == null) {
            throw new IllegalArgumentException("Recipient ID must not be null");
        }

        // Сохраните сообщение в базе данных
        chatMessageService.save(message);

        // Отправка сообщения пользователю
        messagingTemplate.convertAndSendToUser (message.getRecipientId(), "/queue/messages", message);
    }

    @GetMapping("/message/{senderId}/{recipientId}") // Fixed parameter name
    public ResponseEntity<List<ChatMessage>> findChatMessage(
        @PathVariable("senderId") String senderId,
        @PathVariable("recipientId") String recipientId // Fixed parameter name
    ){
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat"; // шаблон chat.html в resources/templates/
    }
}
