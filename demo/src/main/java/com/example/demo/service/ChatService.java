package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.models.Message;

@Service
public class ChatService {

    public Message processMessage(Message message) {
        // Здесь может быть любая бизнес-логика, например, валидация, логирование и т.д.
        // В этом примере просто возвращаем сообщение обратно
        return message;
    }
}