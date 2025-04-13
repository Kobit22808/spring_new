package com.example.demo.service;

import com.example.demo.entity.Comand;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ComandRepository comandRepository;

    // Метод для создания поста
    @Transactional
    public Post createPost(Long comandId, Post post, User user) {
        Comand comand = comandRepository.findById(comandId)
                .orElseThrow(() -> new RuntimeException("Команда не найдена"));

        // Проверка, является ли пользователь лидером команды
        if (!comand.getLeader().getId().equals(user.getId())) {
            throw new RuntimeException("Только лидер команды может создавать посты");
        }

        post.setComand(comand);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    // Метод для получения всех постов
    @Transactional
    public List<Post> getAllPosts() {
        return postRepository.findAll(); // Предполагается, что у вас есть метод findAll в репозитории
    }

    // Метод для получения постов по команде
    public List<Post> getPostsByComand(Comand comand) {
        return postRepository.findByComand(comand);
    }

    // Метод для получения поста по ID
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null); // Возвращаем пост или null, если не найден
    }

    public void updatePost(Post post) {
        postRepository.save(post); // Метод save() обновляет пост, если он уже существует
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id); // Удаляем пост по ID
    }
}