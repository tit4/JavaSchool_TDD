package com.db.javaschool.tdd.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Yury
 */
public class ArticleService {
    private volatile List<Article> articles;

    int counter = 0;

    public void createArticles(Set<Article> articles) {
        this.articles = new ArrayList<>(articles);
    }

    public Article findNextArticleForModeration() {
        int id = counter++;
        if (id < articles.size()) {
            Article article = articles.get(id);
            article.setId(id);
            article.setModerated();
            return article;
        }
        throw new IllegalStateException("No more articles available");
    }
}
