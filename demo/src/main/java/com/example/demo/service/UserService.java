package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserRoleEntity;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository; // Импортируйте репозиторий ролей
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    //    public User addUser (String username, String firstName, String lastName, String patronymic, String email, String password, LocalDate birth, Set<UserRoleEntity> roles) {
//        User user = new User();
//        user.setUsername(username);
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setPatronymic(patronymic);
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password)); // Хеширование пароля
//        user.setBirth(birth); // Установка даты рождения
//        user.setCreatedAt(LocalDate.now()); // Установка даты создания
//        user.setUserRoles(roles); // Установка ролей
//        return userRepository.save(user);
//    }
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
}