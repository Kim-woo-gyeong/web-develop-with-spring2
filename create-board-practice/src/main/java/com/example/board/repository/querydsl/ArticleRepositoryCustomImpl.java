package com.example.board.repository.querydsl;

import com.example.board.domain.Article;
import com.example.board.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {
    public ArticleRepositoryCustomImpl(){
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        // 제너릭타입 적는게 중요함.
        JPQLQuery<String> query = from(article)
                .distinct()
                .select(article.hashtag) // 만약에 select(article) 하면 , findAll 과 동일함.
                .where(article.hashtag.isNotNull());
        return query.fetch();
    }
}
