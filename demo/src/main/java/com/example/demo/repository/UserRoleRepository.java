package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.entity.UserRoleEntity; // Убедитесь, что импортируете правильный класс
import org.springframework.data.jpa.repository.JpaRepository;

import javax.lang.model.element.Name;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findByName(Role role);
//    Optional<UserRoleEntity> findByName(Name name);
}