package com.example.board.repository;

import com.example.board.domain.ArticleComment;
import com.example.board.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
         JpaRepository<ArticleComment, Long>
        , QuerydslPredicateExecutor<ArticleComment>
        , QuerydslBinderCustomizer<QArticle> {

    List<ArticleComment> findByArticle_Id(Long articleId);

    // java8 부터 interface 를 직접 구현이 가능해짐.
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true); // 지정한 프로퍼티에 대해서만 검색되도록. defualt = false
        bindings.including(root.title, root.content, root.createdAt, root.createdBy);

        // 이 경우에는 '%' 를 개발자가 직접 적용시켜줘야 함
        //bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // ex) like '${vale}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // ex) like '%${vale}%'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    };
}