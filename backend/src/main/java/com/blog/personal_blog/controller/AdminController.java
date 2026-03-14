package com.blog.personal_blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.personal_blog.common.R;
import com.blog.personal_blog.entity.Article;
import com.blog.personal_blog.entity.Comment;
import com.blog.personal_blog.entity.Like;
import com.blog.personal_blog.entity.User;
import com.blog.personal_blog.mapper.LikeMapper;
import com.blog.personal_blog.service.ArticleService;
import com.blog.personal_blog.service.CommentService;
import com.blog.personal_blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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
    private final LikeMapper likeMapper;

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
    @Transactional
    public R<Void> deleteUser(@PathVariable Long id) {
        LambdaQueryWrapper<Like> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(Like::getUserId, id);
        likeMapper.delete(likeWrapper);
        
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getUserId, id);
        commentService.remove(commentWrapper);
        
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getAuthorId, id);
        articleService.remove(articleWrapper);
        
        userService.removeById(id);
        return R.success();
    }

    @GetMapping("/articles")
    public R<Object> getArticleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return R.success(articleService.getArticleList(page, size, null));
    }

    @DeleteMapping("/article/{id}")
    public R<Void> deleteArticle(@PathVariable Long id) {
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comment::getArticleId, id);
        commentService.remove(commentWrapper);
        
        LambdaQueryWrapper<Like> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(Like::getTargetType, "article")
                .eq(Like::getTargetId, id);
        likeMapper.delete(likeWrapper);
        
        articleService.removeById(id);
        return R.success();
    }

    @GetMapping("/comments")
    public R<Object> getCommentList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return R.success(commentService.getAllComments(page, size));
    }

    @DeleteMapping("/comment/{id}")
    public R<Void> deleteComment(@PathVariable Long id) {
        LambdaQueryWrapper<Like> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(Like::getTargetType, "comment")
                .eq(Like::getTargetId, id);
        likeMapper.delete(likeWrapper);
        
        commentService.removeById(id);
        return R.success();
    }
}
