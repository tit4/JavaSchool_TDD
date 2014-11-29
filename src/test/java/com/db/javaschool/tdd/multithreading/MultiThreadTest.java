package com.db.javaschool.tdd.multithreading;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

public class MultiThreadTest {

    public static final int THREAD_COUNT = 10;
    public static final int ARTICLE_COUNT = 250;

    @Test (timeout = 100)
    public void findNextArticleForModerationStressTest() throws Exception {

        final ArticleService articleService = new ArticleService();

        // Create test data and callable tasks
        //
        final Set<Article> testArticles = new HashSet<>();

        final Collection<Callable<Article>> tasks = new ArrayList<>();
        for (int i = 0; i < ARTICLE_COUNT; i++) {
            // Test data
            testArticles.add(new Article());

            // Tasks - each task makes exactly one service invocation.
            tasks.add(new Callable<Article>() {
                public Article call() throws Exception {
                    return articleService.findNextArticleForModeration();
                }
            });
        }
        articleService.createArticles(testArticles);

        // Execute tasks
        //
        final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        // invokeAll() blocks until all tasks have run...
        final List<Future<Article>> futures = executorService.invokeAll(tasks);
        assertEquals(ARTICLE_COUNT, futures.size());

        // Assertions
        //
        final Set<Integer> articleIds = new HashSet<>(ARTICLE_COUNT);
        for (Future<Article> future : futures) {
            // get() will throw an exception if an exception was thrown by the service.
            Article article = future.get();
            // Did we get an article?
            assertNotNull(article);

            // Is the article id unique (see Set.add() javadoc)?
            assertEquals("Id " + article.getId() + " is not unique within id set " + articleIds, true, articleIds.add(article.getId()));
        }
        // Did we get the right number of article ids?
        assertEquals(ARTICLE_COUNT, articleIds.size());
    }
}
