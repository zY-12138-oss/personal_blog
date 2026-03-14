package com.blog.personal_blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.personal_blog.common.R;
import com.blog.personal_blog.entity.Article;
import com.blog.personal_blog.service.ArticleService;
import com.blog.personal_blog.utils.JwtUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final JwtUtil jwtUtil;

    @GetMapping("/list")
    public R<Page<Article>> getArticleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId
    ) {
        return R.success(articleService.getArticleList(page, size, categoryId));
    }

    @GetMapping("/search")
    public R<Page<Article>> searchArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        return R.success(articleService.searchArticles(page, size, keyword));
    }

    @GetMapping("/hot")
    public R<List<Article>> getHotArticles(@RequestParam(defaultValue = "10") int limit) {
        return R.success(articleService.getHotArticles(limit));
    }

    @GetMapping("/{id}")
    public R<Article> getArticle(@PathVariable Long id) {
        return R.success(articleService.getArticleById(id));
    }

    @PostMapping
    public R<Article> createArticle(@RequestBody CreateArticleRequest request, HttpServletRequest httpRequest) {
        Long authorId = getUserIdFromToken(httpRequest);
        if (authorId == null) {
            return R.error("请先登录后再发布文章");
        }
        Article article = articleService.createArticle(
                authorId,
                request.getTitle(),
                request.getContent(),
                request.getSummary(),
                request.getCategoryId(),
                request.getStatus()
        );
        return R.success(article);
    }

    @PutMapping("/{id}")
    public R<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request, HttpServletRequest httpRequest) {
        Long authorId = getUserIdFromToken(httpRequest);
        if (authorId == null) {
            return R.error("请先登录后再编辑文章");
        }
        Article article = articleService.updateArticle(
                id,
                authorId,
                request.getTitle(),
                request.getContent(),
                request.getSummary(),
                request.getCategoryId(),
                request.getStatus()
        );
        return R.success(article);
    }

    @DeleteMapping("/{id}")
    public R<Void> deleteArticle(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long authorId = getUserIdFromToken(httpRequest);
        if (authorId == null) {
            return R.error("请先登录后再删除文章");
        }
        articleService.deleteArticle(id, authorId);
        return R.success();
    }

    @PostMapping("/{id}/like")
    public R<Void> likeArticle(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return R.error("请先登录后再点赞");
        }
        articleService.likeArticle(id, userId);
        return R.success();
    }

    @DeleteMapping("/{id}/like")
    public R<Void> unlikeArticle(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return R.error("请先登录后再取消点赞");
        }
        articleService.unlikeArticle(id, userId);
        return R.success();
    }

    @GetMapping("/{id}/has-liked")
    public R<Boolean> hasLikedArticle(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        if (userId == null) {
            return R.success(false);
        }
        return R.success(articleService.hasLikedArticle(id, userId));
    }

    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        return null;
    }

    @Data
    public static class CreateArticleRequest {
        private String title;
        private String content;
        private String summary;
        private Long categoryId;
        private String status;
    }

    @Data
    public static class UpdateArticleRequest {
        private String title;
        private String content;
        private String summary;
        private Long categoryId;
        private String status;
    }
}
