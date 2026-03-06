package com.blog.personal_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.personal_blog.entity.Comment;
import com.blog.personal_blog.entity.Like;
import com.blog.personal_blog.entity.User;
import com.blog.personal_blog.exception.BusinessException;
import com.blog.personal_blog.mapper.CommentMapper;
import com.blog.personal_blog.mapper.LikeMapper;
import com.blog.personal_blog.service.CommentService;
import com.blog.personal_blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserService userService;
    private final LikeMapper likeMapper;

    @Override
    public List<Comment> getCommentsByArticleId(Long articleId) {
        return baseMapper.selectCommentsByArticleId(articleId);
    }

    @Override
    public Comment createComment(Long articleId, Long userId, String content, Long parentId) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setLikes(0);
        comment.setStatus("approved");
        save(comment);
        
        Comment savedComment = getById(comment.getId());
        if (savedComment != null) {
            User user = userService.getById(userId);
            if (user != null) {
                savedComment.setUsername(user.getUsername());
            }
        }
        return savedComment;
    }

    @Override
    public void deleteComment(Long id, Long userId) {
        Comment comment = getById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除此评论");
        }
        removeById(id);
    }

    @Override
    public Page<Comment> getAllComments(int page, int size) {
        Page<Comment> commentPage = new Page<>(page, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getCreatedAt);
        return page(commentPage, wrapper);
    }

    @Override
    @Transactional
    public void likeComment(Long commentId, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "comment")
                .eq(Like::getTargetId, commentId);
        
        if (likeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已经点过赞了");
        }
        
        Like like = new Like();
        like.setUserId(userId);
        like.setTargetType("comment");
        like.setTargetId(commentId);
        likeMapper.insert(like);
        
        Comment comment = getById(commentId);
        comment.setLikes(comment.getLikes() + 1);
        updateById(comment);
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "comment")
                .eq(Like::getTargetId, commentId);
        
        Like like = likeMapper.selectOne(wrapper);
        if (like == null) {
            throw new BusinessException("还没有点赞");
        }
        
        likeMapper.deleteById(like.getId());
        
        Comment comment = getById(commentId);
        comment.setLikes(comment.getLikes() - 1);
        updateById(comment);
    }

    @Override
    public boolean hasLikedComment(Long commentId, Long userId) {
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "comment")
                .eq(Like::getTargetId, commentId);
        return likeMapper.selectCount(wrapper) > 0;
    }
}
