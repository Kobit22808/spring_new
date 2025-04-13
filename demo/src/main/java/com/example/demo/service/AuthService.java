package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
//
//    public Long login(String username, String password) {
//        try {
//            User user = userRepository.findByUsername(username)
//                    .orElseThrow(() -> new RuntimeException("User  not found"));
//
//            // Логируем введенный пароль и хешированный пароль из базы данных
//            logger.debug("Attempting to authenticate user: {}", username);
//            logger.debug("Entered password: {}", password);
//            logger.debug("Stored hashed password: {}", user.getPassword());
//            logger.debug("Password matches: {}", passwordEncoder.matches(password, user.getPassword()));
//
//            // Проверяем хешированный пароль
//            if (!passwordEncoder.matches(password, user.getPassword())) {
//                logger.warn("Authentication failed for user: {}. Passwords do not match.", username);
//                throw new RuntimeException("Invalid username or password");
//            }
//
//            // Если пароль верный, аутентифицируем пользователя
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            logger.info("User  {} authenticated successfully.", username);
//            return user.getId(); // Возвращаем ID пользователя
//        } catch (LockedException e) {
//            logger.error("User  account is locked: {}", username);
//            throw new RuntimeException("User  account is locked");
//        } catch (Exception e) {
//            logger.error("An error occurred during authentication for user {}: {}", username, e.getMessage());
//            throw new RuntimeException("An error occurred during authentication: " + e.getMessage());
//        }
//    }


    public Long login(String username, String password) {
        try {
            // Проверяем, существует ли пользователь
            Optional<User> optionalUser  = userRepository.findByUsername(username);
            if (!optionalUser .isPresent()) {
                throw new RuntimeException("User  not found");
            }

            User user = optionalUser .get(); // Извлекаем объект User

            // Проверяем хешированный пароль
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid username or password");
            }

            // Если пароль верный, аутентифицируем пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return user.getId(); // Возвращаем ID пользователя
        } catch (LockedException e) {
            throw new RuntimeException("User  account is locked");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during authentication: " + e.getMessage());
        }
    }

}