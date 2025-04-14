package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Comand;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRoleEntity;
import com.example.demo.repository.ComandRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserChatRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.entity.UserChatStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ComandRepository comandRepository;

    @Autowired
    private UserChatRepository repository;

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDate.now());
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public UserRoleEntity findOrCreateRole(Role role) {
        Optional<UserRoleEntity> optionalUserRole = userRoleRepository.findByName(role);
        return optionalUserRole.orElseGet(() -> {
            UserRoleEntity newRole = new UserRoleEntity(role);
            return userRoleRepository.save(newRole);
        });
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        return null;
    }

    public List<Comand> getUserComands(User user) {
        return comandRepository.findByMembers(user.getId());
    }

    public Comand getComand(Long id) {
        return comandRepository.findById(id).orElse(null);
    }

    public void addMemberToComand(Long comandId, Long userId) {
        Comand comand = getComand(comandId);
        // Logic to add member to command
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void saveUser(User user) {
        user.setStatus(UserChatStatus.ONLINE);
        repository.save(user);
    }

    public void disconnect(User user) {
        var storedUser = repository.findById(user.getId())
                .orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(UserChatStatus.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(UserChatStatus.ONLINE);
    }
}
