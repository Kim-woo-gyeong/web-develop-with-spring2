package com.example.board.repository;

import com.example.board.config.JpaConfig;
import com.example.board.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Disabled
@ActiveProfiles("testdb")
// 자동으로 testdb 가 실행되지 않고 설정된 db를 불러오도록 함.
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("jpa 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
public class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    // 생성자주입패턴 생성
    public JpaRepositoryTest(
                            @Autowired ArticleRepository articleRepository
                           ,@Autowired ArticleCommentRepository articleCommentRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void crudTest1(){
        // Given

        // When
        List<Article> articles = articleRepository.findAll();
        // Then
        Assertions.assertThat(articles)
                  .isNotNull().hasSize(501);
    }

    @DisplayName("insert 테스트")
    @Test
    void crudTest2(){
        // Given
        long previousCount = articleRepository.count();

        // When
         Article savedArticle = articleRepository.save(Article.of("new Title", "new Content", "#new HashTag"));

         // Then
        Assertions.assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void crudTest3(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashTag = "#Springboot";
        article.setHashTag(updateHashTag);

        // When
        // Step2. save -> saveAndFlush
        Article savedArticle = articleRepository.saveAndFlush(article);
        /* Step1 : 이렇게만 하면 @DataJpaTest 가 모든 @Test 에 대한 Transaction 을
                   묶고 있음. 기본적인 transaction 형태는 rollback.
                   그래서 jpa 의 .save() 가 필요없는 작업일 경우에는
                   생략되는 경우가 발생. 정상적으로 update 가 안됨.
        * */


        // Then
        Assertions.assertThat(savedArticle).hasFieldOrPropertyWithValue("hashTag", updateHashTag);
    }

    @DisplayName("delete 테스트")
    @Test
    void crudTest4(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        long previousCount = article.getArticleComments().size();

        System.out.println(previousCount);

        // When
        articleRepository.delete(article);

        // Then
        Assertions.assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        Assertions.assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - previousCount);
    }
}
