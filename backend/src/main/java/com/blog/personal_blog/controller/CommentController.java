package com.blog.personal_blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.personal_blog.common.R;
import com.blog.personal_blog.entity.Comment;
import com.blog.personal_blog.service.CommentService;
import com.blog.personal_blog.utils.JwtUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @GetMapping("/article/{articleId}")
    public R<List<Comment>> getCommentsByArticle(@PathVariable Long articleId) {
        return R.success(commentService.getCommentsByArticleId(articleId));
    }

    @PostMapping
    public R<Comment> createComment(@RequestBody CreateCommentRequest request, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        Comment comment = commentService.createComment(
                request.getArticleId(),
                userId,
                request.getContent(),
                request.getParentId()
        );
        return R.success(comment);
    }

    @DeleteMapping("/{id}")
    public R<Void> deleteComment(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromToken(httpRequest);
        commentService.deleteComment(id, userId);
        return R.success();
    }

    @GetMapping("/list")
    public R<Page<Comment>> getAllComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return R.success(commentService.getAllComments(page, size));
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
    public static class CreateCommentRequest {
        private Long articleId;
        private String content;
        private Long parentId;
    }
}
