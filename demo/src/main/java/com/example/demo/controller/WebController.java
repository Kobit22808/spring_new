package com.example.demo.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/messages") // Указываем базовый путь для этого контроллера
public class WebController {

    @GetMapping
    public String message() {
        return "message"; // Это будет соответствовать файлу message.html в папке static
    }
}