package com.example.demo.service;

import com.example.demo.entity.Comand;
import com.example.demo.entity.User;
import com.example.demo.entity.UserComandLink;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.UserComandLinkRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComandService {

    @Autowired
    private ComandRepository comandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserComandLinkRepository userComandLinkRepository;

    public List<Comand> getComands() {
        return comandRepository.findAll();
    }

    public Comand getComand(Long id) {
        return comandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Команда не найдена")); // Обработка случая, когда команда не найдена
    }


    public Comand createComand(String title, String description, Long leaderId) {
        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("User  not found"));

        Comand comand = new Comand();
        comand.setTitle(title);
        comand.setDescription(description);
        comand.setLeader(leader);

        return comandRepository.save(comand);
    }

    public void addMemberToComand(Long comandId, Long userId) {
        Comand comand = comandRepository.findById(comandId)
                .orElseThrow(() -> new RuntimeException("Команда не найдена"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Проверяем, существует ли уже связь
        Optional<UserComandLink> existingLink = userComandLinkRepository.findByUserAndComand(user, comand);
        if (existingLink.isPresent()) {
            throw new RuntimeException("Пользователь уже является участником команды");
        }

        UserComandLink link = new UserComandLink();
        link.setUser (user);
        link.setComand(comand);

        // Сохраняем связь между пользователем и командой
        userComandLinkRepository.save(link);
    }

    public List<User> getUsersInComand(Long comandId) {
        return comandRepository.findUsersByComandId(comandId);
    }
}