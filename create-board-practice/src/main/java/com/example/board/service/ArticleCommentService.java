package com.example.board.service;

import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.ArticleCommentUpdateDto;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleUpdateDto;
import com.example.board.repository.ArticleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComment(Long articleId) {
        return null;
    }

    public void saveArticleComment(ArticleCommentDto dto) {
    }

    public void updateArticleComment(long articleCommentId, ArticleCommentUpdateDto dto) {
    }

    public void deleteArticleComment(long articleCommentId) {
    }
}
