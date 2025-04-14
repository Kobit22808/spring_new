package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.User;
import com.example.demo.entity.UserChatStatus;

import java.util.List;

public interface UserChatRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findAllByStatus(@Param("status") UserChatStatus status);
}
