package com.example.demo.dto;

import lombok.Data;

@Data
public class TaskDto {
    private String title;
    private String text;
    private Long comandId;
    private Long userId;
}