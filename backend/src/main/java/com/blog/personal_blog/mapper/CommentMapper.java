package com.blog.personal_blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.personal_blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*, u.username as username, u.avatar as avatar " +
            "FROM comments c " +
            "LEFT JOIN users u ON c.user_id = u.id " +
            "WHERE c.article_id = #{articleId} AND c.status = 'approved' " +
            "ORDER BY c.created_at DESC")
    List<Comment> selectCommentsByArticleId(@Param("articleId") Long articleId);
}
