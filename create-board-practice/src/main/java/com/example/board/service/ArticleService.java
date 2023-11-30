package com.example.board.service;

import com.example.board.domain.Article;
import com.example.board.domain.type.SearchType;
import com.example.board.dto.ArticleCommentDto;
import com.example.board.dto.ArticleDto;
import com.example.board.dto.ArticleWithCommentsDto;
import com.example.board.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticle(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()){
           /*
            * ArticleDto의 from 함수를 mapper 로 관리되어도 됨.
            * 그러나 이번 강의에서는 ArticleDto를 무게감 있게 만들었음.
            * 현재 까지는 Entity가 노출되지 않음.
            * */
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long Id) {
        return articleRepository.findById(Id)
                                .map(ArticleWithCommentsDto::from)
                                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + Id));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try{
        /*
        articleRepository.getReferenceById(dto.id()) VS articleRepository.findById
        두가지의 역할은 비슷하지만 내부동작이 다름.
        * */
            Article article = articleRepository.getReferenceById(dto.id());
            if(dto.title() != null){ article.setTitle(dto.title()); }
            if(dto.content() != null){ article.setContent(dto.content()); }
            if(dto.hashtag() != null){ article.setHashtag(dto.hashtag()); }
            // save는 필요없음. @Transaction 으로 묶여 있기 때문에
            // 영속성 context 는 (?) 변화를 감지하여 자동으로 update 쿼리를 실행시킴.          
        }catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. - dto: {}"+ dto);
        }
    }

    public void deleteArticle(Long Id) {
        articleRepository.deleteById(Id);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }
}
