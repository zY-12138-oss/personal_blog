package com.blog.personal_blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.personal_blog.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {

    Page<Article> getArticleList(int page, int size, Long categoryId);

    Page<Article> searchArticles(int page, int size, String keyword);

    List<Article> getHotArticles(int limit);

    Article getArticleById(Long id);

    Article createArticle(Long authorId, String title, String content, String summary, Long categoryId, String status);

    Article updateArticle(Long id, Long authorId, String title, String content, String summary, Long categoryId, String status);

    void deleteArticle(Long id, Long authorId);

    void likeArticle(Long articleId, Long userId);

    void unlikeArticle(Long articleId, Long userId);

    boolean hasLikedArticle(Long articleId, Long userId);
}
