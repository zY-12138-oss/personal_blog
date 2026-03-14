package com.blog.personal_blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.personal_blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    
    @Select("SELECT a.*, u.username as authorName, u.avatar as authorAvatar, c.name as categoryName " +
            "FROM articles a " +
            "LEFT JOIN users u ON a.author_id = u.id " +
            "LEFT JOIN categories c ON a.category_id = c.id " +
            "WHERE a.id = #{id}")
    Article selectArticleWithDetails(@Param("id") Long id);
}
