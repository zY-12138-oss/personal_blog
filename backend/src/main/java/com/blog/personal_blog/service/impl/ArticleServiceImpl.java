package com.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.personal_blog.entity.Article;
import com.blog.personal_blog.entity.Category;
import com.blog.personal_blog.entity.Like;
import com.blog.personal_blog.entity.User;
import com.blog.personal_blog.exception.BusinessException;
import com.blog.personal_blog.mapper.ArticleMapper;
import com.blog.personal_blog.mapper.LikeMapper;
import com.blog.personal_blog.service.ArticleService;
import com.blog.personal_blog.service.CategoryService;
import com.blog.personal_blog.service.UserService;
import com.blog.personal_blog.utils.XssUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final LikeMapper likeMapper;

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
    public Page<Article> searchArticles(int page, int size, String keyword) {
        Page<Article> articlePage = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, "published");
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w
                    .like(Article::getTitle, keyword)
                    .or()
                    .like(Article::getContent, keyword)
                    .or()
                    .like(Article::getSummary, keyword)
            );
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
    public List<Article> getHotArticles(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, "published");
        wrapper.orderByDesc(Article::getViews);
        wrapper.last("LIMIT " + limit);
        
        List<Article> articles = list(wrapper);
        
        for (Article article : articles) {
            User author = userService.getById(article.getAuthorId());
            if (author != null) {
                article.setAuthorName(author.getUsername());
            }
        }
        
        return articles;
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
        article.setTitle(XssUtil.sanitize(title));
        article.setContent(XssUtil.sanitizeRichText(content));
        article.setSummary(XssUtil.sanitize(summary));
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
        
        if (title != null) article.setTitle(XssUtil.sanitize(title));
        if (content != null) article.setContent(XssUtil.sanitizeRichText(content));
        if (summary != null) article.setSummary(XssUtil.sanitize(summary));
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

    @Override
    @Transactional
    public void likeArticle(Long articleId, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "article")
                .eq(Like::getTargetId, articleId);
        
        if (likeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已经点过赞了");
        }
        
        Like like = new Like();
        like.setUserId(userId);
        like.setTargetType("article");
        like.setTargetId(articleId);
        likeMapper.insert(like);
        
        Article article = getById(articleId);
        article.setLikes(article.getLikes() + 1);
        updateById(article);
    }

    @Override
    @Transactional
    public void unlikeArticle(Long articleId, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "article")
                .eq(Like::getTargetId, articleId);
        
        Like like = likeMapper.selectOne(wrapper);
        if (like == null) {
            throw new BusinessException("还没有点赞");
        }
        
        likeMapper.deleteById(like.getId());
        
        Article article = getById(articleId);
        article.setLikes(article.getLikes() - 1);
        updateById(article);
    }

    @Override
    public boolean hasLikedArticle(Long articleId, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "article")
                .eq(Like::getTargetId, articleId);
        return likeMapper.selectCount(wrapper) > 0;
    }
}
