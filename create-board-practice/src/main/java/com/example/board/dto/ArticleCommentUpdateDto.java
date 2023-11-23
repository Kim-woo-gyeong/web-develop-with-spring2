package com.example.board.dto;

import java.io.Serializable;
import java.time.LocalDateTime;


public record ArticleCommentUpdateDto(String content){
    public static ArticleCommentUpdateDto of(String content){
        return new ArticleCommentUpdateDto(content);
    }
}