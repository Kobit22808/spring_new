package com.example.demo.repository;

import com.example.demo.entity.Comand;
import com.example.demo.entity.User;
import com.example.demo.entity.UserComandLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserComandLinkRepository extends JpaRepository<UserComandLink, Long> {
    Optional<UserComandLink> findByUserAndComand(User user, Comand comand);
}