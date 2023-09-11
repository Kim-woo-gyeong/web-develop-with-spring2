package com.example.board.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("article 컨트롤러 테스트")
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {
    private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[View][GET] 게시글 리스트 페이지 테스트")
    @Test
    void createTest1() throws Exception {
        // given
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))   // 해당 경로에 view 가 있는지 확인
                .andExpect(model().attributeExists("articles"));

        // when & then
    }

    @Disabled("개발중")
    @DisplayName("[View][GET] 게시글 상세 페이지 테스트")
    @Test
    void createTest2() throws Exception {
        // given
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));

        // when & then
    }

    @Disabled("개발중")
    @DisplayName("[View][GET] 게시글 검색 전용 페이지 테스트")
    @Test
    void createTest3() throws Exception {
        // given
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));

        // when & then
    }

    @Disabled("개발중")
    @DisplayName("[View][GET] 게시글 해시태그 검색 페이지 테스트")
    @Test
    void createTest4() throws Exception {
        // given
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"));

        // when & then
    }
}
