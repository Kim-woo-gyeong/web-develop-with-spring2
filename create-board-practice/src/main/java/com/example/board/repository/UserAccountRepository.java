package com.example.board.repository;

import com.example.board.domain.QUserAccount;
import com.example.board.domain.UserAccount;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserAccountRepository extends
        JpaRepository<UserAccount, String>
        , QuerydslPredicateExecutor<UserAccount>
        , QuerydslBinderCustomizer<QUserAccount> {

    @Override
    default void customize(QuerydslBindings bindings, QUserAccount root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.userId, root.email, root.memo, root.nickname, root.createdAt, root.createdBy);

        // 이 경우에는 '%' 를 개발자가 직접 적용시켜줘야 함
        bindings.bind(root.userId).first(StringExpression::containsIgnoreCase); // ex) like '%${vale}%'
        bindings.bind(root.email).first(StringExpression::containsIgnoreCase); // ex) like '%${vale}%'
        bindings.bind(root.memo).first(StringExpression::containsIgnoreCase); // ex) like '%${vale}%'
        bindings.bind(root.nickname).first(StringExpression::containsIgnoreCase); // ex) like '%${vale}%'
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    };
}
