package com.example.board.domain;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Table()
public class Article {
    private long id;
    private String title;   // 제목
    private String content; // 본문

    private LocalDateTime createdAt;    // 생성일자
    private String createdBy;           // 셍성자
    private LocalDateTime modifiedAt;   // 수정일자
    private String modifiedBy;          // 수정자
}
