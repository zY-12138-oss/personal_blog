package com.blog.personal_blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.personal_blog.common.R;
import com.blog.personal_blog.entity.Article;
import com.blog.personal_blog.entity.Comment;
import com.blog.personal_blog.entity.User;
import com.blog.personal_blog.service.ArticleService;
import com.blog.personal_blog.service.CommentService;
import com.blog.personal_blog.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/stats")
    public R<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userService.count());
        stats.put("articleCount", articleService.count());
        stats.put("commentCount", commentService.count());
        
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, "published");
        stats.put("publishedArticleCount", articleService.count(articleWrapper));
        
        return R.success(stats);
    }

    @GetMapping("/users")
    public R<Object> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return R.success(userService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size)
        ));
    }

    @DeleteMapping("/user/{id}")
    public R<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return R.success();
    }
}
