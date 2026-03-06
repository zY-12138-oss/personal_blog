package com.blog.personal_blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.personal_blog.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<Comment> getCommentsByArticleId(Long articleId);

    Comment createComment(Long articleId, Long userId, String content, Long parentId);

    void deleteComment(Long id, Long userId);

    Page<Comment> getAllComments(int page, int size);

    void likeComment(Long commentId, Long userId);

    void unlikeComment(Long commentId, Long userId);

    boolean hasLikedComment(Long commentId, Long userId);
}
