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
@Entity
public class ArticleComment extends AuditingFields{
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

    /*방법1.*/
//    @Embedded AAA aa;
//    class AAA {};

    /*방법2.*/
    // AuditingFields 를 생성하여 상속받는 방식.
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
