package com.example.board.dto;

import java.time.LocalDateTime;
public record ArticleDto (
        LocalDateTime createdAt,
        String createdBy,
        String title,
        String content,
        String hashTag){
    public static ArticleDto of(LocalDateTime createdAt, String createdBy, String title, String content, String hashTag){
        return new ArticleDto(createdAt, createdBy, title, content, hashTag);
    }
}