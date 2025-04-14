package com.example.demo.chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    public void processMessage( // Corrected method name
        @Payload ChatMessage chatMessage
    ){
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
            chatMessage.getRecipientId(), // Updated to getRecipientId()
            "/queue/messages",
            null
        );
    }

    @GetMapping("/message/{senderId}/{recipientId}") // Fixed parameter name
    public ResponseEntity<List<ChatMessage>> findChatMessage(
        @PathVariable("senderId") String senderId,
        @PathVariable("recipientId") String recipientId // Fixed parameter name
    ){
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
