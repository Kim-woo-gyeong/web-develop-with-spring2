package com.example.board.controller;

import com.example.board.domain.Article;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.response.ArticleResponse;
import com.example.board.dto.response.ArticleWithCommentResponse;
import com.example.board.service.ArticleService;
import com.example.board.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
            ModelMap modelMap
    ){
        // svc.searchArticle return type 은 dto.
        // dto 는 모든 엔티티 정보를 다 담고 있음. 그래서 response 로 한번더 가공한 것을 return 함.
        Page<ArticleResponse> articles = articleService.searchArticle(searchType, searchValue, pageable)
                                                       .map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        modelMap.addAttribute("articles", articles);
        modelMap.addAttribute("paginationBarNumbers", barNumbers);
        modelMap.addAttribute("searchTypes", SearchType.values());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String getArticle(
            @PathVariable Long articleId,
            ModelMap modelMap
    ){
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticle(articleId));

        modelMap.addAttribute("article",article);
        modelMap.addAttribute("articleComments", article.articleCommentsResponses());
        modelMap.addAttribute("totalCount", articleService.getArticleCount());

        return "articles/detail";
    }
}
