package com.example.board.service;

import com.example.board.domain.Article;
import com.example.board.domain.ArticleComment;
import com.example.board.domain.UserAccount;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.ArticleCommentUpdateDto;
import com.example.board.dto.UserAccountDto;
import com.example.board.repository.ArticleCommentRepository;
import com.example.board.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        ArticleComment expected = createArticleComment("content");

        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

       //When
      List<ArticleCommentDto> actual = svc.searchArticleComments(articleId);

       //Then
       assertThat(actual).hasSize(1)
                        .first()
                        .hasFieldOrPropertyWithValue("content", expected.getContent());

       then(articleCommentRepository).should().findByArticle_Id(articleId);
   }

    @DisplayName("댓글정보를 입력하면 댓글이 생성된다.")
    @Test
    void articleIdAndContent_createArticleComment(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        //When
        svc.saveArticleComment(dto);

        //Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));

    }

    @DisplayName("댓글Id와 내용을 입력하면 댓글이 수정된다.")
    @Test
    void articleCommentIdAndContent_UpdateArticleComment(){
        // Given
        String oldContent = "content";
        String updateContent = "new content";
        ArticleComment expected = createArticleComment(oldContent);
        ArticleCommentDto dto = createArticleCommentDto(updateContent);

        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(expected);

        //When
        svc.updateArticleComment(dto);

        //Then
        assertThat(expected.getContent())
                .isNotEqualTo(oldContent)
                .isEqualTo(updateContent);

        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면 경고 로그를 찍고 아무것도 안한다.")
    @Test
    void notExistsArticleComment_ReturnErrorLogAndDoesNothing(){
        // Given
        ArticleCommentDto dto = createArticleCommentDto("new comment two");

        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        //When
        svc.updateArticleComment(dto);

        //Then
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("댓글 ID를 입력하면, 댓글이 삭제된다.")
    @Test
    void articleCommentId_DeleteArticleComment(){
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);

        //When
        svc.deleteArticleComment(articleCommentId);

        //Then
        then(articleCommentRepository).should().deleteById(articleCommentId);
    }

    private Article createArticle() {
       return Article.of(
               createUserAccount(),
               "title",
               "content",
               "#java"
       );
    }

    private ArticleComment createArticleComment(String content) {
       return ArticleComment.of(
               createUserAccount(),
               Article.of(createUserAccount(), "title", "content", "#java"),
               content
       );
    }

    private UserAccount createUserAccount() {
       return UserAccount.of(
               "kwg",
               "password",
               "email",
               "nickname",
               "this is memo"
       );
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
       return ArticleCommentDto.of(
               1L,
               1L,
               createUserAccountDto(),
               content,
               LocalDateTime.now(),
               "kwg",
               LocalDateTime.now(),
               "kwg"
       );
    }

    private UserAccountDto createUserAccountDto() {
       return UserAccountDto.of(
               1L,
               "kwg",
               "password",
               "email",
               "nickname",
               "this is memo",
               LocalDateTime.now(),
               "kwg",
               LocalDateTime.now(),
               "kwg"
       );
    }
}
