package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "comands")
public class Comand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description; // Поле для описания

    @ManyToOne // Связь с пользователем, который является лидером команды
    @JoinColumn(name = "leader_id")
    private User leader; // Поле для лидера команды

    @ManyToMany // Связь многие-ко-многим с пользователями
    @JoinTable(
            name = "comand_users",
            joinColumns = @JoinColumn(name = "comand_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>(); // Список участников команды

    @OneToMany(mappedBy = "comand") // Связь с UserComandLink
    private Set<UserComandLink> userComandLinks = new HashSet<>(); // Связь с UserComandLink
}