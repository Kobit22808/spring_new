package com.example.demo.service;

import com.example.demo.entity.Comand;
import com.example.demo.repository.ComandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComandService {
    @Autowired
    private ComandRepository comandRepository;

    public List<Comand> getAllComands() {
        return comandRepository.findAll();
    }

    public Comand saveComand(Comand comand) {
        return comandRepository.save(comand);
    }

    // Другие методы для работы с командами
}