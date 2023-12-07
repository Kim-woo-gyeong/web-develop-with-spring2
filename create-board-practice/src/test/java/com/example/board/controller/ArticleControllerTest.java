package com.example.board.controller;

import com.example.board.config.SecurityConfig;
import com.example.board.domain.Article;
import com.example.board.domain.UserAccount;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.dto.UserAccountDto;
import com.example.board.service.ArticleService;
import com.example.board.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("article 컨트롤러 테스트")
// 401 에러를 대응함.
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
    // articleService 를 테스트에서 배제하고
    // 컨트롤러 테스트가 api의 입출력만 보게끔 하기 위해 mocking 함.
    // 메소드 파라미터로 넣을 수 없음.
    // 그래서 mockMvc 는 생성자로 주입하고 mockBean 은 선언함.
    // @Autowired private MockMvc mvc; <- 이렇게 선언해도 됨.
     private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }
    // api 데이터의 입출력만 보이게 하기 위해 mocking 을 해야함.
    // 그래서 연결을 끊어주기 위해 사용.
    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;


    @DisplayName("[View][GET] 게시글 리스트 페이지 테스트 - 정상호출")
    @Test
    void givenNothing_whenSearchArticle_thenReturnArticle() throws Exception {
        // given
        // eq(null) : 빈값만 조회되도록.
        given(articleService.searchArticle(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
        // when & then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))   // 해당 경로에 view 가 있는지 확인
                .andExpect(model().attributeExists("articles"));

        then(articleService).should().searchArticle(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
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
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchTypes"));

        then(articleService).should().getArticle(articleId);
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    void givenSearchValue_whenArticleWithSearchValue_thenReturnArticles() throws Exception{
        // given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "제목";
        given(articleService.searchArticle(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        // when
        mvc.perform(
                get("/articles")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchValue)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));

        //then
        then(articleService).should().searchArticle(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[View][GET] 게시글 리스트 페이지(게시판) 테스트 - 페이징 & 정렬기능")
    @Test
    void givenNothing_whenPaginationBarNumbers_thenArticlesWithPagination() throws Exception {
        // given
        String sortName = "title";
        String direction = "DESC";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1,2,3,4,5);

        given(articleService.searchArticle(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // when & then
        mvc.perform(
                get("/articles")
                        .queryParam("page", String.valueOf(pageNumber))
                        .queryParam("size", String.valueOf(pageSize))
                        .queryParam("sort", sortName + "," + direction)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(view().name("articles/index"))
        .andExpect(model().attributeExists("articles"))
        .andExpect(model().attribute("paginationBarNumbers", barNumbers));

        then(articleService).should().searchArticle(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
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

    @DisplayName("[View][GET] 게시글 해시태그 검색 페이지 테스트")
    @Test
    void givenNothing_whenSearchHashtag_thenReturnEmpyPage() throws Exception {
        // given
        List<String> hashtags = List.of("#java", "#spring", "#mySql");
        given(articleService.searchArticlesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(articleService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        // when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        then(articleService).should().searchArticlesViaHashtag(eq(null), any(Pageable.class));
        then(articleService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
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
