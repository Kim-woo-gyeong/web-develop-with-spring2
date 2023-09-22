package com.example.board.service;

import com.example.board.domain.Article;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleUpdateDto;
import com.example.board.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    // mock 을 주입하는 대상에 @InjectMocks 그 외는 @Mock
    // 생성자 주입은 안되는 오류가 있음.
   @InjectMocks
   private ArticleService svc;

   @Mock
   private ArticleRepository articleRepository;

   @DisplayName("게시글을 검색하면, 게시글이 조회된다.")
   @Test
    void searchArticle_ReturnArticles(){
       //Given

       //When
      // STEP1.
       //List<ArticleDto> articles = svc.searchArticle(SearchType.TITLE, "search keyword");
      // STEP2.
      /*
      * Page는 사용 가능한 요소의 개수와 페이지의 개수를 알고 있음. -- JPA 페이징 방식.
      * List는 추가 count 쿼리가 실행되지 않음.
       * */
      Page<ArticleDto> articles = svc.searchArticle(SearchType.TITLE, "search keyword");

       //Then
       Assertions.assertThat(articles).isNotNull();
   }

   @DisplayName("각 게시글 페이지로 이동")
   @Test
   void returnArticle(){
       // Given

       //When
       ArticleDto article = svc.searchArticle(1L);
       //Then
       Assertions.assertThat(article).isNotNull();
   }

   @DisplayName("게시글 정보를 입력하면, 게시글이 생성된다.")
   @Test
   void articleIdAndContent_createArticle(){
      // Given
       // mockito 단위테스트 방식.
       // ArticleRepositry 는 저장 시, return 되는 값이 없기 때문에 willDoNothing.given 으로 해야함.
       // STEP1. save 는 return 값이 있음. 그래서 willDoNothing 을 쓰면 안됨.
       //willDoNothing().given(articleRepository).save(any(Article.class));
       given(articleRepository.save(ArgumentMatchers.any(Article.class))).willReturn(null); // 예상.

      //When
      svc.saveArticle(ArticleDto.of(LocalDateTime.now(), "Kwg", "title", "content","#JAVA"));

      //Then
       then(articleRepository).should().save(any(Article.class));

   }

   @DisplayName("게시글 ID와 수정 내용을 입력하면, 게시글이 수정된다.")
   @Test
   void articleIdAndContent_UpdateArticle(){
       // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);
       //When
       svc.updateArticle (1L, ArticleUpdateDto.of("update Title", "update Content", "#Python"));

       //Then
       then(articleRepository).should().save(any(Article.class));
   }

   @DisplayName("게시글 ID를 입력하면, 게시글이 삭제된다.")
   @Test
   void articleId_DeleteArticle(){
       // Given
       willDoNothing().given(articleRepository).delete(any(Article.class));

       //When
       svc.deleteArticle(1L);

       //Then
       then(articleRepository).should().delete(any(Article.class));
   }
}
