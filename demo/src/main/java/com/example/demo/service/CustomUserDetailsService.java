package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Внедряем PasswordEncoder

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User  not found with username: {}", username);
                    return new UsernameNotFoundException("User  not found with username: " + username);
                });

        logger.debug("User  found: {}", user);

        // Получаем роли пользователя
        List<GrantedAuthority> authorities = user.getUserRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName().name())) // Используем getName() и затем name() // Используем name() для получения строки
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities); // Добавляем роли
    }

    // Метод для создания пользователей в памяти (если это необходимо)
    public void createDefaultUsers() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        // Сохраните пользователя в базе данных через userRepository

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        // Сохраните администратора в базе данных через userRepository
    }

    // Метод для создания PasswordEncoder

}