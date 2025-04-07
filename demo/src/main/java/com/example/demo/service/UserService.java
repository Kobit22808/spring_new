package com.example.demo.service;

import com.example.demo.entity.Comand;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserRoleEntity;
import com.example.demo.entity.User;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository; // Импортируйте репозиторий ролей
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository; // Добавьте это поле

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ComandRepository comandRepository;

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Хеширование пароля
        user.setCreatedAt(LocalDate.now()); // Установка даты создания
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User  not found with username: " + username));
    }

    public UserRoleEntity findOrCreateRole(Role role) {
        // Получаем имя роли
//        String roleName = role.name();

        // Ищем роль в репозитории
        Optional<UserRoleEntity> optionalUserRole = userRoleRepository.findByName(role); // Передаем объект Role
        return optionalUserRole.orElseGet(() -> {
            UserRoleEntity newRole = new UserRoleEntity(role);
            return userRoleRepository.save(newRole);
        });
        // Если роль найдена, возвращаем ее
//        if (optionalUserRole.isPresent()) {
//            return optionalUserRole.get();
//        } else {
//            // Если роль не найдена, создаем новую
//            UserRoleEntity newRole = new UserRoleEntity(role);
//            return roleRepository.save(newRole); // Сохраняем новую роль и возвращаем ее
//        }
    }
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public User getCurrentUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, аутентифицирован ли пользователь
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName(); // Получаем имя пользователя из аутентификации
            System.out.println("Current username: " + username); // Логируем текущее имя пользователя

            // Ищем пользователя в базе данных по имени
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        System.out.println("User  not found for username: " + username); // Логируем, если пользователь не найден
                        return new RuntimeException("User  not found");
                    });
        }

        // Если пользователь не аутентифицирован, возвращаем null
        return null;
    }


    public List<Comand> getUserComands(User user) {
        return comandRepository.findByMembers(user.getId()); // Передаем ID пользователя
    }

    public Comand getComand(Long id) {
        return comandRepository.findById(id).orElse(null);
    }

    public void addMemberToComand(Long comandId, Long userId) {
        Comand comand = getComand(comandId);
        // Логика добавления участника в команду
    }

    public List<User> getUsers() {
        return userRepository.findAll(); // Возвращает всех пользователей
    }


}