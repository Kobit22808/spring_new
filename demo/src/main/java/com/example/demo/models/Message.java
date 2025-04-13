package com.example.demo.models;

public class Message {
    private String from;
    private String text;
    private String time; // Если хотите хранить время отправки

    // Геттеры и сеттеры
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}