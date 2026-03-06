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

    @GetMapping("/{id}")
    public R<Article> getArticle(@PathVariable Long id) {
        return R.success(articleService.getArticleById(id));
    }

    @PostMapping
    public R<Article> createArticle(@RequestBody CreateArticleRequest request, HttpServletRequest httpRequest) {
        Long authorId = getUserIdFromToken(httpRequest);
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
        articleService.deleteArticle(id, authorId);
        return R.success();
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
