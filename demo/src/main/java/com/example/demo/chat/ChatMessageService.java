package com.example.demo.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true // createNewRoomIfNotExists
        ).orElseThrow(); // Вы можете создать свое собственное исключение
        chatMessage.setChatId(chatId);
        chatMessage.setTimestamp(LocalDateTime.now().toString()); // Установите текущее время
        return repository.save(chatMessage);
    }
    
    public List<ChatMessage> findChatMessages(
        String senderId, String recipientId
    ){
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>()); // Updated to use findByChatId
    }
}
