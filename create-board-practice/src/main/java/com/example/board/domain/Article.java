package com.example.board.domain;

import java.time.LocalDateTime;

public class Article {
    private long id;
    private String title;   // 제목
    private String content; // 본문

    private LocalDateTime createdAt;    // 생성일자
    private String createdBy;           // 셍성자
    private LocalDateTime modifiedAt;   // 수정일자
    private String modifiedBy;          // 수정자
}
