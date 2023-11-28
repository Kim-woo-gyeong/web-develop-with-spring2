package com.example.board.controller;

import com.example.board.config.SecurityConfig;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.dto.UserAccountDto;
import com.example.board.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("article 컨트롤러 테스트")
// 401 에러를 대응함.
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
     private final MockMvc mvc;

    // articleService 를 테스트에서 배제하고
    // 컨트롤러 테스트가 api의 입출력만 보게끔 하기 위해 mocking 함.
    // 메소드 파라미터로 넣을 수 없음.
    // 그래서 mockMvc 는 생성자로 주입하고 mockBean 은 선언함.
    // @Autowired private MockMvc mvc; <- 이렇게 선언해도 됨.
    @MockBean private ArticleService articleService;

    // api 데이터의 입출력만 보이게 하기 위해 mocking 을 해야함.
    // 그래서 연결을 끊어주기 위해 사용.
    @MockBean private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[View][GET] 게시글 리스트 페이지 테스트 - 정상호출")
    @Test
    void givenNothing_whenSearchArticle_thenReturnArticle() throws Exception {
        // given
        // eq(null) : 빈값만 조회되도록.
        given(articleService.searchArticle(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))   // 해당 경로에 view 가 있는지 확인
                .andExpect(model().attributeExists("articles"));

        then(articleService).should().searchArticle(eq(null), eq(null), any(Pageable.class));
    }

    @DisplayName("[View][GET] 게시글 상세 페이지 테스트 - 정상호출")
    @Test
    void givenArticleId_whenGetArticle_thenReturnArticle() throws Exception {
        // given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto(articleId));

        // when & then
        mvc.perform(get("/articles/"+articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));

        then(articleService).should().getArticle(articleId);
    }

    @Disabled("개발중")
    @DisplayName("[View][GET] 게시글 검색 전용 페이지 테스트")
    @Test
    void createTest3() throws Exception {
        // given
        // ToDo:추후에 추가.
        // when & then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));

    }

    @Disabled("개발중")
    @DisplayName("[View][GET] 게시글 해시태그 검색 페이지 테스트")
    @Test
    void createTest4() throws Exception {
        // given
        // ToDo:추후에 추가.

        // when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"));

    }

    private ArticleWithCommentsDto createArticleWithCommentsDto(Long articleId) {
        return ArticleWithCommentsDto.of(
                articleId
                , createUserAccountDto(articleId)
                , createArticle().getArticleComments()
                        .stream().map(ArticleCommentDto::from)
                        .collect(Collectors.toSet())
                , "this is title."
                , "this is content."
                , "#dto"
                , LocalDateTime.now()
                , "kkk"
                , LocalDateTime.now()
                , "kkk"
        );
    }

    private UserAccountDto createUserAccountDto(Long articleId){
        return UserAccountDto.of(
                 articleId
                ,"kwgyeong"
                , "password"
                ,"kwgyeong0423@gmail.com"
                , "kwg"
                ,"this is memo"
                , LocalDateTime.now()
                , "kkk"
                , LocalDateTime.now()
                , "kkk"
        );
    }

    private Article createArticle(){
        return Article.of(
                 createUserAccount()
                ,"this is title"
                , "this is content"
                , "#springboot"
        );
    }

    private UserAccount createUserAccount(){
        return UserAccount.of(
                "kwgyeong"
                , "password"
                ,"kwgyeong0423@gmail.com"
                , "kwg"
                ,"this is memo"
        );
    }


    private ArticleWithCommentsDto createArticleWithCommentsDto(Long articleId) {
        return ArticleWithCommentsDto.of(
                 articleId
                ,createUserAccountDto()
                ,Set.of()
                ,"게시판만들기 관련 질문드립니다."
                ,"of와 from 의 역할이 무엇인가요?"
                ,"#spring boot"
                , LocalDateTime.now()
                , "kwgyeong"
                , LocalDateTime.now()
                , "kwgyeong"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L
                ,"kwgyeong"
                ,"password!@#"
                ,"kwgyeong0423@naver.com"
                ,"굥2"
                ,"this is memo"
                ,LocalDateTime.now()
                ,"kwgyeong"
                , LocalDateTime.now()
                ,"kwgyeong"
        );
    }
}
