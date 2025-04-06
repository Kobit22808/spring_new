package com.example.demo.repository;

import com.example.demo.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByName(String name); // Метод для поиска роли по имени
}