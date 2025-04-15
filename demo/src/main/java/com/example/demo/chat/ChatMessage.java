package com.example.demo.chat;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Авто-генерация ID
    private Long id; // Тип Long для идентификатора
    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private String timestamp;

    // Adding the setChatId method
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    // Adding getter methods
    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }
}
