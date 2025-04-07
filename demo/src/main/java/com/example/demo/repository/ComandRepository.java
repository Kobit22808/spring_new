package com.example.demo.repository;

import com.example.demo.entity.Comand;
import com.example.demo.entity.User;
import org .springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComandRepository extends JpaRepository<Comand, Long> {

    @Query("SELECT u FROM User u JOIN UserComandLink ucl ON u.id = ucl.user.id WHERE ucl.comand.id = :comandId")
    List<User> findUsersByComandId(@Param("comandId") Long comandId);

    @Query("SELECT c FROM Comand c JOIN UserComandLink ucl ON c.id = ucl.comand.id WHERE ucl.user.id = :userId")
    List<Comand> findByMembers(@Param("userId") Long userId);
}