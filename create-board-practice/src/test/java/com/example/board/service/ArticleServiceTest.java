package com.example.board.service;

import com.example.board.domain.Article;
import com.example.board.domain.UserAccount;
import com.example.board.domain.constant.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.dto.UserAccountDto;
import com.example.board.repository.ArticleRepository;
import com.example.board.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

   @Mock
   private UserAccountRepository userAccountRePository;

   @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
   @Test
    void noSearchArticle_ReturnArticlePage(){
       //Given
       Pageable pageable = Pageable.ofSize(20);
       given(articleRepository.findAll(pageable)).willReturn(Page.empty());

       //When
      // STEP1.
       //List<ArticleDto> articles = svc.searchArticle(SearchType.TITLE, "search keyword");
      // STEP2.
      /*
      * Page는 사용 가능한 요소의 개수와 페이지의 개수를 알고 있음. -- JPA 페이징 방식.
      * List는 추가 count 쿼리가 실행되지 않음.
       * */
      // Page<ArticleDto> articles = svc.searchArticle(SearchType.TITLE, "search keyword");
       // STEP3. Page 적용
       Page<ArticleDto> articles = svc.searchArticle(null, null, pageable);

       //Then
       Assertions.assertThat(articles).isNotNull();
       then(articleRepository).should().findAll(pageable);
   }

    @DisplayName("게시글 ID로 조회하면, 댓글 달긴 게시글을 반환한다.")
    @Test
    void givenArticleId_whenGetArticle_thenReturnArticleWithComments(){
        //given
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        //when
        ArticleWithCommentsDto dto = svc.getArticleWithComments(articleId);

        //then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());

        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 달린 게시글이 없으면, 예외를 던진다.")
    @Test
    void givenArticleId_whenNothingArticleWithComments_thenThrowException(){
       //given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        //when
        Throwable t = catchThrowable(() -> svc.getArticleWithComments(articleId));

        //then
        assertThat(t).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void searchParameters_ReturnArticlePage(){
        //Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        //When
        Page<ArticleDto> articles = svc.searchArticle(searchType, searchKeyword, pageable);

        //Then
        Assertions.assertThat(articles).isNotNull();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

   @DisplayName("게시글을 조회하면 게시글을 반환한다.")
   @Test
   void returnArticle() {
       // Given
       Long articleId = 1L;
       Article article = createArticle();
       given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

       //When
       ArticleDto dto = svc.getArticle(articleId);

       //Then
       Assertions.assertThat(dto)
               .hasFieldOrPropertyWithValue("title",article.getTitle())
               .hasFieldOrPropertyWithValue("content", article.getContent())
               .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());

       then(articleRepository).should().findById(articleId);
   }

    @DisplayName("게시글이 없으면, 예외를 던진다.")
    @Test
    void notExistsArticle_ThrowException() {
        // Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        //When
        Throwable t = Assertions.catchThrowable(() -> svc.getArticleWithComments(articleId));
        //ArticleWithCommentsDto dto = svc.getArticleWithComments(articleId);

        //Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
//        assertThat(dto)
//                .withFailMessage("게시글이 없습니다 - articleId: " + articleId)
//                        .isNotNull();

        then(articleRepository).should().findById(articleId);
    }


   @DisplayName("게시글 정보를 입력하면, 게시글이 생성된다.")
   @Test
   void articleIdAndContent_createArticle(){
      // Given
       // mockito 단위테스트 방식.
       // ArticleRepositry 는 저장 시, return 되는 값이 없기 때문에 willDoNothing.given 으로 해야함.
       // STEP1. save 는 return 값이 있음. 그래서 willDoNothing 을 쓰면 안됨.
       //willDoNothing().given(articleRepository).save(any(Article.class));
       ArticleDto dto = createArticleDto();
       // 사용자가 있는지 우선 체크함.
       given(userAccountRePository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
       given(articleRepository.save(ArgumentMatchers.any(Article.class))).willReturn(createArticle()); // 예상.

      //When
      svc.saveArticle(dto);

      //Then
       then(userAccountRePository).should().getReferenceById(dto.userAccountDto().userId());
       then(articleRepository).should().save(any(Article.class));

   }

   @DisplayName("게시글 ID와 수정 내용을 입력하면, 게시글이 수정된다.")
   @Test
   void articleIdAndContent_UpdateArticle(){
       // Given
       Article article = createArticle();
       ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");

        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
       //When
       svc.updateArticle(dto.id(), dto);

       //Then
       assertThat(article).hasFieldOrPropertyWithValue("title",dto.title())
                        .hasFieldOrPropertyWithValue("content",dto.content())
                        .hasFieldOrPropertyWithValue("hashtag",dto.hashtag());

       then(articleRepository).should().getReferenceById(dto.id());
   }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고로그를 찍고 아무것도 하지 않는다.")
    @Test
    void notExistsArticleIdAndContent_ReturnErrorLogAndDoNothing(){
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");

        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);
        //When
        svc.updateArticle(dto.id(), dto);

        //Then
        then(articleRepository).should().getReferenceById(dto.id());
    }

   @DisplayName("게시글 ID를 입력하면, 게시글이 삭제된다.")
   @Test
   void articleId_DeleteArticle(){
       // Given
       Long articleId = 1L;
       willDoNothing().given(articleRepository).deleteById(articleId);

       //When
       svc.deleteArticle(articleId);

       //Then
       then(articleRepository).should().deleteById(articleId);
   }

   // 해시태그 검색 페이지 구현하기
   @DisplayName("[해시태그페이지]검색어 없이 해시태그 검색하면, 빈 페이지를 반환한다.")
   @Test
   void givenNothing_whenSearchViaHashtag_thenRequrnPageEmpty(){
       //given
       Pageable pageable = Pageable.ofSize(20);

       // when
       Page<ArticleDto> articles = svc.searchArticlesViaHashtag(null, pageable);

       // then
       //empty 페이지를 보내줄거면 persistent 영역까지 내려갈 필요없다
       assertThat(articles).isEqualTo(Page.empty(pageable));
       then(articleRepository).shouldHaveNoInteractions();
   }

    @DisplayName("[해시태그페이지]해시태그 검색하면, 게시판 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchViaHashtag_thenReturnArticle(){
       // given
        Pageable pageable = Pageable.ofSize(20);
        String hashtag = "#java";
        given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));

        // when
        Page<ArticleDto> articles = svc.searchArticlesViaHashtag(hashtag, pageable);

        // then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtag(hashtag, pageable);
    }

    /*
        중복되는 해시태그도 있을거고 없는 게시글도 있을것임.
        유니크하게 구분된 해시태그 리스트를 반환하는 것을 개발할것..
    */
    @DisplayName("[해시태그페이지]해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다.")
    @Test
    void givenNothing_whenDistinctHAshtag_thenReturnListHashtag(){
        //given
        // JPA 는 도메인 단위로 출력을 함. 특정 필드만 뽑아서 출력하는 쿼리를 만들 수 없다.
        // -> 이것을 해결하기 위해서 Querydsl 을 사용해야 함!
        List<String> expected = List.of("#java", "#spring", "#boot");
        given(articleRepository.findAllDistinctHashtags()).willReturn(expected);

        // when
        List<String> actual = svc.getHashtags();

        // then
        assertThat(actual).isEqualTo(expected);
        then(articleRepository).should().findAllDistinctHashtags();
    }

    private Article createArticle() {
        Article article = Article.of(createUserAccount(), "title", "content", "#java");
        ReflectionTestUtils.setField(article, "id", 1L);
       return article;
    }

    private UserAccount createUserAccount() {

       return UserAccount.of("kwg", "password", "emial","KWG", null);
    }

    private ArticleDto createArticleDto() {

       return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
       return ArticleDto.of(
               1L,
               createUserAccountDto(),
               title,
               content,
               hashtag,
               LocalDateTime.now(),
               "kwg",
               LocalDateTime.now(),
               "kwg"
       );
    }

    private UserAccountDto createUserAccountDto() {
       return UserAccountDto.of(
               "kwg",
               "password",
               "email",
               "nickName",
               "this is memo",
               LocalDateTime.now(),
               "kwg",
               LocalDateTime.now(),
               "kwg"
       );
    }


}
