package com.example.demo.controller;

import com.example.demo.entity.Comand;
import com.example.demo.service.ComandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comands")
public class ComandController {
    @Autowired
    private ComandService comandService;

    @GetMapping
    public List<Comand> getAllComands() {
        return comandService.getAllComands();
    }

    @PostMapping
    public Comand createComand(@RequestBody Comand comand) {
        return comandService.saveComand(comand);
    }

    // Другие методы для управления командами
}