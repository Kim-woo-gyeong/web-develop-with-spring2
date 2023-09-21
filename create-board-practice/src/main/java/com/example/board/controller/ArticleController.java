package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/articles")
@Controller
public class ArticleController {
    @GetMapping
    public String articles(ModelMap modelMap){
        modelMap.addAttribute("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap modelMap){
        modelMap.addAttribute("article", "article"); // Todo:추후에 dto 개발 후 수정.
        modelMap.addAttribute("articleComments", List.of());
        return "articles/detail";
    }
}
