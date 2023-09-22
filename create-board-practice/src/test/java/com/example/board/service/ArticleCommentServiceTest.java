package com.example.board.service;

import com.example.board.domain.Article;
import com.example.board.domain.ArticleComment;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.ArticleCommentUpdateDto;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleUpdateDto;
import com.example.board.repository.ArticleCommentRepository;
import com.example.board.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
public class ArticleCommentServiceTest {
    // mock 을 주입하는 대상에 @InjectMocks 그 외는 @Mock
    // 생성자 주입은 안되는 오류가 있음.
   @InjectMocks
   private ArticleCommentService svc;

   @Mock
   private ArticleRepository articleRepository;
    @Mock
    private ArticleCommentRepository articleCommentRepository;

   @DisplayName("게시글 ID로 조회하면 댓글이 조회된다.")
   @Test
    void articleId_ReturnArticleComments(){
       //Given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(
                Optional.of(Article.of("title", "content", "#Java"))
        );

       //When
      List<ArticleCommentDto> articleComments = svc.searchArticleComment(articleId);

       //Then
       Assertions.assertThat(articleComments).isNotNull();
       then(articleRepository).should().findById(articleId);
   }

    @DisplayName("댓글정보를 입력하면 댓글이 생성된다.")
    @Test
    void articleIdAndContent_createArticleComment(){
        // Given
        given(articleCommentRepository.save(ArgumentMatchers.any(ArticleComment.class))).willReturn(null); // 예상.

        //When
        svc.saveArticleComment(ArticleCommentDto.of(LocalDateTime.now(), "Kwg", LocalDateTime.now(), "Kwg","comment content"));

        //Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));

    }

    @DisplayName("댓글Id와 내용을 입력하면 댓글이 수정된다.")
    @Test
    void articleCommentIdAndContent_UpdateArticleComment(){
        // Given
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        //When
        svc.updateArticleComment(1L, ArticleCommentUpdateDto.of("update Content"));

        //Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 ID를 입력하면, 댓글이 삭제된다.")
    @Test
    void articleCommentId_DeleteArticleComment(){
        // Given
        willDoNothing().given(articleCommentRepository).delete(any(ArticleComment.class));

        //When
        svc.deleteArticleComment(1L);

        //Then
        then(articleCommentRepository).should().delete(any(ArticleComment.class));
    }
}
