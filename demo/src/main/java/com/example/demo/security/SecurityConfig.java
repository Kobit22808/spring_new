package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
            .csrf(csrf -> csrf.disable()) // Отключаем CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/error", "/api/users", "/api/login", "/api/register").permitAll() // Разрешить доступ к страницам без аутентификации
                .requestMatchers("/chat/**").authenticated() // Разрешить доступ к чату только для аутентифицированных пользователей
                .requestMatchers("/comands/**").authenticated() // Доступ для администраторов
                .requestMatchers("/tasts/**").authenticated() // Доступ для авторизованных пользователей
                .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login") // Перенаправление после выхода
                .permitAll() // Разрешить доступ к выходу
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}