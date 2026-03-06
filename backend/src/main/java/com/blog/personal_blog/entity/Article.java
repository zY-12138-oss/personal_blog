package com.blog.personal_blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("articles")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String summary;

    private Long authorId;

    private Long categoryId;

    private Integer views;

    private Integer likes;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String categoryName;
}
