package com.example.demo.chat;

import java.sql.Date;

import jakarta.persistence.Entity;
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
    private String id;
    private String chatId; // Field name remains as is
    private String senderid;
    private String recipientid;
    private String content;
    private Date timestamp;

    // Adding the setChatId method
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    // Adding getter methods
    public String getSenderId() {
        return senderid;
    }

    public String getRecipientId() {
        return recipientid;
    }
}
