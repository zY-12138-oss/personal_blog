package com.blog.personal_blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.personal_blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
