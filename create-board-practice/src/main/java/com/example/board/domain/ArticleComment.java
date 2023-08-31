package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // optional : 필수여부
    // cascade
    @Setter
    @ManyToOne(optional = false)
    private Article article;        // 게시글 id

    @Setter @Column(nullable = false, length = 2000)
    private String content;         // 본문

    @CreatedDate
    private LocalDateTime createdAt;    // 생성일자
    @CreatedBy
    @Column(length = 100) private String createdBy;           // 셍성자
    @LastModifiedDate
    private LocalDateTime modifiedAt;   // 수정일자
    @LastModifiedBy
    @Column(length = 100) private String modifiedBy;          // 수정자

    protected ArticleComment(){}

    // @NoArgsConstructor 어노테이션으로 대체가능.
    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content){
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
