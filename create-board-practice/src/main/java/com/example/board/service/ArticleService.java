package com.example.board.service;

import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticle(SearchType searchType, String searchKeyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(Long Id) {
        return null;
    }

    public void saveArticle(ArticleDto dto) {
    }

    public void updateArticle(long Id, ArticleUpdateDto dto) {
    }

    public void deleteArticle(long Id) {
    }
}
