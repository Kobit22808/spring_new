package com.example.demo.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> { // Измените на Long, если id - Long

    List<ChatMessage> findByChatId(String chatId); // Оставьте только этот метод
}
