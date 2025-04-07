package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF (для упрощения)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/error").permitAll() // Разрешить доступ к главной, входу и регистрации
                        .requestMatchers("/comands/create").hasRole("ADMIN") // Доступ только для ADMIN к странице создания команды
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Указать страницу входа
                        .defaultSuccessUrl("/") // Перенаправление после успешного входа
                        .failureUrl("/login?error=true") // Перенаправление при ошибке входа
                        .permitAll() // Разрешить доступ к странице входа
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .permitAll() // Разрешить доступ к выходу
                );

        return http.build();
    }

}