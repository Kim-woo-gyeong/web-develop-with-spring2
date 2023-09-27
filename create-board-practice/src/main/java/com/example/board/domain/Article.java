package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@ToString(callSuper = true) // 상속받은 정보까지 출력.
@Getter
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // DB에 저장 후 기본 키 값을 구할 수 있다.
    private Long id;

    @Setter @Column(nullable = false)
    private String title;   // 제목

    @Setter @Column(nullable = false, length = 10000)
    private String content; // 본문

    @Setter @ManyToOne(optional = false)
    private UserAccount userAccount;    // 유저정보(ID)

    // article 에 대한 comments 를 중복허용하지 않고 볼 수 있도록 하겠다.
    // article 테이블에서 왔다는 것을 명시.
    // casecade : 엄청난 종속성을 가지므로 유연하게 수정하는 것이 힘듬. 원치않는 데이터소실이 있을 수 있음.
    //            하나의 article 이 삭제되면 같이 삭제될 수 있도록.
    @ToString.Exclude
    @OrderBy("id")
    // 양방향 ID
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    @Setter
    private String hashtag; // 해시태그

    protected Article(){}

    private Article(UserAccount userAccount, String title, String content, String hashtag){
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    public static Article of(UserAccount userAccount, String title, String content, String hashtag){
        return new Article(userAccount, title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
