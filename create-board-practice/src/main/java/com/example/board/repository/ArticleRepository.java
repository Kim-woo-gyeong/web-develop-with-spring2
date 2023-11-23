package com.example.board.repository;

import com.example.board.domain.Article;
import com.example.board.domain.QArticle;
import com.example.board.domain.type.SearchType;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
         JpaRepository<Article, Long>
        , QuerydslPredicateExecutor<Article>
        , QuerydslBinderCustomizer<QArticle>
{
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContainig(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);


    @Override
    // java8 부터 interface 를 직접 구현이 가능해짐.
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true); // 지정한 프로퍼티에 대해서만 검색되도록. defualt = false
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);

        // 이 경우에는 '%' 를 개발자가 직접 적용시켜줘야 함
        //bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // ex) like '${vale}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // ex) like '%${vale}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    };
}