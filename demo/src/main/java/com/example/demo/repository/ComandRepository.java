package com.example.demo.repository;

import com.example.demo.entity.Comand;
import org .springframework.data.jpa.repository.JpaRepository;

public interface ComandRepository extends JpaRepository<Comand, Long> {
}