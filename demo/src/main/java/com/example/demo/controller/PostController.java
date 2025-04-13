package com.example.demo.controller;

import com.example.demo.entity.Comand;
import com.example.demo.entity.Image;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.service.ComandService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ComandService comandService;

    @Autowired
    private UserService userService;

    // Метод для отображения всех постов
    @GetMapping
    public String showAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts(); // Получаем все посты

        // Форматируем дату для каждого поста
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (Post post : posts) {
            post.setFormattedCreatedAt(post.getCreatedAt().format(formatter)); // Форматируем дату

            // Преобразуем изображения в строку Base64
            List<Image> images = post.getImages();
            if (images != null && !images.isEmpty()) {
                List<String> base64Images = new ArrayList<>();
                for (Image img : images) {
                    String base64Image = Base64.getEncoder().encodeToString(img.getData());
                    base64Images.add("data:image/jpeg;base64," + base64Image);
                }
                post.setFormattedImages(base64Images); // Устанавливаем отформатированные изображения
            }
        }

        model.addAttribute("posts", posts);
        return "all_posts"; // Имя HTML-шаблона для страницы со всеми постами
    }

    // Метод для отображения формы создания поста
    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        Comand comand = comandService.getComand(1L); // Получаем команду по ID
        model.addAttribute("comand", comand); // Добавляем команду в модель
        return "create_post"; // Имя HTML-шаблона для создания поста
    }

    //    // Метод для создания поста
    @PostMapping("/create")
    public String createPost(@RequestParam Long comandId, @ModelAttribute Post post, @RequestParam("image") MultipartFile image) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (image == null || image.isEmpty()) {
            return "redirect:/posts/create?comandId=" + comandId; // Возврат на страницу создания поста
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; // Перенаправление на страницу входа
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // Имя пользователя
        User user = userService.findByUsername(username);

        try {
            byte[] imageBytes = image.getBytes(); // Получаем байты изображения
            Image img = new Image(); // Создаем новый объект Image
            img.setData(imageBytes); // Устанавливаем данные изображения
            img.setPost(post); // Устанавливаем связь с постом

            // Добавляем изображение в список изображений поста
            if (post.getImages() == null) {
                post.setImages(new ArrayList<>()); // Инициализируем список, если он еще не инициализирован
            }
            post.getImages().add(img); // Добавляем изображение в список
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки
        }

        postService.createPost(comandId, post, user); // Создаем пост
        return "redirect:/"; // Перенаправление на страницу со всеми постами
    }

    // Метод для отображения конкретного поста
    @GetMapping("/{id}")
    public String showPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id); // Получаем пост по ID
        if (post == null) {
            return "404"; // Возвращаем страницу 404, если пост не найден
        }
        model.addAttribute("post", post);
        return "post"; // Имя HTML-шаблона для отображения поста
    }

    @GetMapping("/comands/{id}/posts")
    public String showPostsByComand(@PathVariable Long id, Model model, Authentication authentication) {
        Comand comand = comandService.getComandById(id); // Получаем команду по ID
        List<Post> posts = postService.getPostsByComand(comand); // Получаем посты для этой команды



        model.addAttribute("comand", comand);
        model.addAttribute("posts", posts);

        // Проверяем, является ли текущий пользователь лидером команды
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username); // Получаем пользователя по имени
            System.out.println("Leader ID: " + comand.getLeader().getId());
            System.out.println("Current User ID: " + user.getId());
            boolean isLeader = comand.getLeader().getId().equals(user.getId()); // Сравниваем ID лидера с ID текущего пользователя
            model.addAttribute("isLeader", isLeader);
        } else {
            model.addAttribute("isLeader", false); // Если пользователь не аутентифицирован, устанавливаем isLeader в false
        }

        return "comand_posts"; // Имя HTML-шаблона для отображения постов команды
    }

    // Метод для отображения формы редактирования поста
    @GetMapping("/{id}/edit")
    public String showEditPostForm(@PathVariable Long id, Model model, Authentication authentication) {
        Post post = postService.getPostById(id); // Получаем пост по ID
        if (post == null) {
            return "404"; // Возвращаем страницу 404, если пост не найден
        }

        Comand comand = comandService.getComandById(post.getComand().getId()); // Получаем команду по ID поста
        model.addAttribute("comand", comand); // Добавляем команду в модель
        model.addAttribute("post", post); // Добавляем пост в модель

        // Проверяем, является ли текущий пользователь лидером команды
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username); // Получаем пользователя по имени
            boolean isLeader = comand.getLeader().getId().equals(user.getId()); // Сравниваем ID лидера с ID текущего пользователя
            model.addAttribute("isLeader", isLeader);
        } else {
            model.addAttribute("isLeader", false); // Если пользователь не аутентифицирован, устанавливаем isLeader в false
        }

        return "edit_post"; // Используем новый шаблон для редактирования поста
    }

    // Метод для отображения формы редактирования поста
    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Long id, @RequestParam Long comandId, @ModelAttribute Post post, @RequestParam("image") MultipartFile image) {
        // Получаем существующий пост по ID
        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            return "404"; // Возвращаем страницу 404, если пост не найден
        }

        // Обновляем поля существующего поста
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setCreatedAt(LocalDateTime.now()); // Обновляем дату создания, если это необходимо

        // Обработка изображения
        if (image != null && !image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes(); // Получаем байты изображения
                Image img = new Image(); // Создаем новый объект Image
                img.setData(imageBytes); // Устанавливаем данные изображения
                img.setPost(existingPost); // Устанавливаем связь с постом

                // Добавляем изображение в список изображений поста
                if (existingPost.getImages() == null) {
                    existingPost.setImages(new ArrayList<>()); // Инициализируем список, если он еще не инициализирован
                }
                existingPost.getImages().add(img); // Добавляем изображение в список
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки
            }
        }

        // Сохраняем обновленный пост
        postService.updatePost(existingPost); // Предполагается, что у вас есть метод updatePost в сервисе
        return "redirect:/posts/comands/" + comandId + "/posts"; // Перенаправление на страницу с постами команды
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        Post post = postService.getPostById(id); // Получаем пост по ID
        if (post != null) {
            postService.deletePost(id); // Удаляем пост
        }
        return "redirect:/posts"; // Перенаправляем на страницу со списком постов
    }
}