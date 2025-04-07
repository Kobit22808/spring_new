package com.example.demo.repository;

import com.example.demo.entity.Comand;
import com.example.demo.entity.User;
import org .springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComandRepository extends JpaRepository<Comand, Long> {
    List<Comand> findByMembers(User user);
}