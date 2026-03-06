package com.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.personal_blog.entity.Article;
import com.blog.personal_blog.entity.Category;
import com.blog.personal_blog.entity.User;
import com.blog.personal_blog.exception.BusinessException;
import com.blog.personal_blog.mapper.ArticleMapper;
import com.blog.personal_blog.service.ArticleService;
import com.blog.personal_blog.service.CategoryService;
import com.blog.personal_blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public Page<Article> getArticleList(int page, int size, Long categoryId) {
        Page<Article> articlePage = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, "published");
        if (categoryId != null) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Article::getCreatedAt);
        
        Page<Article> resultPage = page(articlePage, wrapper);
        
        for (Article article : resultPage.getRecords()) {
            User author = userService.getById(article.getAuthorId());
            if (author != null) {
                article.setAuthorName(author.getUsername());
            }
            if (article.getCategoryId() != null) {
                Category category = categoryService.getById(article.getCategoryId());
                if (category != null) {
                    article.setCategoryName(category.getName());
                }
            }
        }
        
        return resultPage;
    }

    @Override
    public Article getArticleById(Long id) {
        Article article = baseMapper.selectArticleWithDetails(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        
        article.setViews(article.getViews() + 1);
        updateById(article);
        
        return article;
    }

    @Override
    public Article createArticle(Long authorId, String title, String content, String summary, Long categoryId, String status) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setSummary(summary);
        article.setAuthorId(authorId);
        article.setCategoryId(categoryId);
        article.setViews(0);
        article.setLikes(0);
        article.setStatus(status != null ? status : "draft");
        save(article);
        return article;
    }

    @Override
    public Article updateArticle(Long id, Long authorId, String title, String content, String summary, Long categoryId, String status) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        if (!article.getAuthorId().equals(authorId)) {
            throw new BusinessException("无权限编辑此文章");
        }
        
        if (title != null) article.setTitle(title);
        if (content != null) article.setContent(content);
        if (summary != null) article.setSummary(summary);
        if (categoryId != null) article.setCategoryId(categoryId);
        if (status != null) article.setStatus(status);
        
        updateById(article);
        return article;
    }

    @Override
    public void deleteArticle(Long id, Long authorId) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        if (!article.getAuthorId().equals(authorId)) {
            throw new BusinessException("无权限删除此文章");
        }
        removeById(id);
    }
}
